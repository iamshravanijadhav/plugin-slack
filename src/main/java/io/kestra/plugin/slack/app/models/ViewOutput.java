package io.kestra.plugin.slack.app.models;

import com.slack.api.model.view.View;

import io.kestra.core.models.annotations.PluginProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@Schema(
    title = "View output"
)
public class ViewOutput implements io.kestra.core.models.tasks.Output {
    @Schema(title = "View ID", description = "Unique identifier of the Slack view.")
    @PluginProperty
    String viewId;

    @Schema(title = "View hash", description = "Hash of the Slack view returned by the API.")
    @PluginProperty
    String hash;

    public static ViewOutput from(View view) {
        if (view == null) {
            throw new IllegalStateException("Slack returned no view in the response");
        }

        return ViewOutput.builder()
            .viewId(view.getId())
            .hash(view.getHash())
            .build();
    }
}
