package io.kestra.plugin.slack.app.views;

import java.util.Map;

import com.slack.api.methods.request.views.ViewsOpenRequest;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.annotations.PluginProperty;
import io.kestra.core.models.property.Property;
import io.kestra.core.models.tasks.RunnableTask;
import io.kestra.core.runners.RunContext;
import io.kestra.core.serializers.JacksonMapper;
import io.kestra.plugin.slack.AbstractSlackClientConnection;
import io.kestra.plugin.slack.app.models.ViewOutput;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
                    triggerId: "{{ trigger.body.trigger_id }}"
                    view:
                      type: modal
                      title:
                        type: plain_text
                        text: Hello
                      close:
                        type: plain_text
                        text: Close
                      blocks:
                        - type: section
                          text:
                            type: mrkdwn
                            text: Hello from Kestra!
                """
        )
    }
)
public class Open extends AbstractSlackClientConnection implements RunnableTask<ViewOutput> {
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
    private Property<Map<String, Object>> view;

    @Override
    public ViewOutput run(RunContext runContext) throws Exception {
        var request = ViewsOpenRequest.builder()
            .triggerId(
                runContext.render(this.triggerId).as(String.class).filter(value -> !value.isBlank()).orElseThrow(() -> new IllegalArgumentException("'triggerId' rendered to an empty value, pass the trigger_id from the Slack interaction payload"))
            )
            .viewAsString(JacksonMapper.ofJson().writeValueAsString(runContext.render(this.view).asMap(String.class, Object.class)))
            .build();

        var response = call(runContext, client -> client.viewsOpen(request));
        return ViewOutput.from(response.getView());
    }
}
