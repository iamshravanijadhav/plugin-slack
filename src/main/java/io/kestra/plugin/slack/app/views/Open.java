package io.kestra.plugin.slack.app.views;

import com.slack.api.methods.request.views.ViewsOpenRequest;
import com.slack.api.methods.response.views.ViewsOpenResponse;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.annotations.PluginProperty;
import io.kestra.core.models.property.Property;
import io.kestra.core.models.tasks.RunnableTask;
import io.kestra.core.runners.RunContext;
import io.kestra.plugin.slack.AbstractSlackClientConnection;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Schema(
    title = "Open a Slack view",
    description = "Opens a modal view in Slack using a trigger ID."
)
@Plugin(
    examples = {
        @Example(
            title = "Open a Slack modal",
            full = true,
            code = """
                id: slack_open_view
                namespace: company.team

                tasks:
                  - id: open_view
                    type: io.kestra.plugin.slack.app.views.Open
                    token: "{{ secret('SLACK_TOKEN') }}"
                    triggerId: "{{ triggerId }}"
                    view: |
                      {
                        "type": "modal",
                        "title": {
                          "type": "plain_text",
                          "text": "Hello"
                        },
                        "close": {
                          "type": "plain_text",
                          "text": "Close"
                        },
                        "blocks": [
                          {
                            "type": "section",
                            "text": {
                              "type": "mrkdwn",
                              "text": "Hello from Kestra!"
                            }
                          }
                        ]
                      }
                """
        )
    }
)
public class Open extends AbstractSlackClientConnection implements RunnableTask<Open.Output> {
    @Schema(
        title = "Slack trigger ID",
        description = "Trigger ID returned by Slack when opening a modal."
    )
    @NotNull
    @PluginProperty(group = "main")
    private Property<String> triggerId;

    @Schema(
        title = "View JSON",
        description = "JSON definition of the Slack view to open."
    )
    @NotNull
    @PluginProperty(group = "main")
    private Property<String> view;

    @Override
    public Output run(RunContext runContext) throws Exception {
        ViewsOpenRequest request = ViewsOpenRequest.builder()
            .triggerId(runContext.render(this.triggerId).as(String.class).orElseThrow())
            .viewAsString(runContext.render(this.view).as(String.class).orElseThrow())
            .build();

        ViewsOpenResponse response = call(runContext, client -> client.viewsOpen(request));

        return Output.builder()
            .viewId(response.getView().getId())
            .hash(response.getView().getHash())
            .build();
    }

    @Value
    @Builder
    @Jacksonized
    public static class Output implements io.kestra.core.models.tasks.Output {
        @Schema(title = "View ID")
        @PluginProperty
        String viewId;

        @Schema(title = "View hash")
        @PluginProperty
        String hash;
    }
}