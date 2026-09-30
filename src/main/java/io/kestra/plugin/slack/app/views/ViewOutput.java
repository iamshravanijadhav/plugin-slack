package io.kestra.plugin.slack.app.views;

import com.slack.api.model.view.View;

import io.kestra.core.models.annotations.PluginProperty;
import io.kestra.core.models.tasks.Output;

import io.swagger.v3.oas.annotations.media.Schema;

public class ViewOutput implements Output {
    @Schema(title = "View ID", description = "Unique identifier of the Slack view.")
    @PluginProperty
    private final String viewId;

    @Schema(title = "View hash", description = "Hash of the Slack view returned by the API.")
    @PluginProperty
    private final String hash;

    public ViewOutput(String viewId, String hash) {
        this.viewId = viewId;
        this.hash = hash;
    }

    public String getViewId() {
        return viewId;
    }

    public String getHash() {
        return hash;
    }

    public static ViewOutput from(View view) {
        if (view == null) {
            throw new IllegalStateException("Slack returned no view in the response");
        }

        return new ViewOutput(view.getId(), view.getHash());
    }
}
