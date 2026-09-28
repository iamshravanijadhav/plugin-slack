package io.kestra.plugin.slack.app.views;

import com.slack.api.methods.request.views.ViewsPublishRequest;
import com.slack.api.methods.response.views.ViewsPublishResponse;

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
    title = "Publish a Slack view",
    description = "Publishes a view to a user's Home tab."
)
@Plugin(
    examples = {
        @Example(
            title = "Publish a Slack Home tab view",
            full = true,
            code = """
                id: slack_publish_view
                namespace: company.team

                tasks:
                  - id: publish_view
                    type: io.kestra.plugin.slack.app.views.Publish
                    token: "{{ secret('SLACK_TOKEN') }}"
                    userId: "U1234567890"
                    view: |
                      {
                        "type": "home",
                        "blocks": [
                          {
                            "type": "section",
                            "text": {
                              "type": "mrkdwn",
                              "text": "Welcome to the Home tab!"
                            }
                          }
                        ]
                      }
                """
        )
    }
)
public class Publish extends AbstractSlackClientConnection implements RunnableTask<Publish.Output> {
    @Schema(
        title = "Slack user ID",
        description = "Slack user ID whose Home tab should be updated."
    )
    @NotNull
    @PluginProperty(group = "main")
    private Property<String> userId;

    @Schema(
        title = "View JSON",
        description = "JSON definition of the Slack view to publish."
    )
    @NotNull
    @PluginProperty(group = "main")
    private Property<String> view;

    @Schema(
        title = "View hash",
        description = "Optional hash of the previous view to prevent publishing an outdated view."
    )
    @PluginProperty(group = "advanced")
    private Property<String> hash;

    @Override
    public Output run(RunContext runContext) throws Exception {
        var builder = ViewsPublishRequest.builder()
            .userId(runContext.render(this.userId).as(String.class).orElseThrow())
            .viewAsString(runContext.render(this.view).as(String.class).orElseThrow());

        if (this.hash != null) {
            runContext.render(this.hash).as(String.class).ifPresent(builder::hash);
        }

        ViewsPublishResponse response = call(runContext, client -> client.viewsPublish(builder.build()));

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