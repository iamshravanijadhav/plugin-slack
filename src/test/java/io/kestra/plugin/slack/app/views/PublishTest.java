package io.kestra.plugin.slack.app.views;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.models.property.Property;
import io.kestra.core.runners.RunContextFactory;
import io.kestra.core.utils.IdUtils;
import io.kestra.core.utils.TestsUtils;
import io.kestra.plugin.slack.FakeWebhookController;
import io.kestra.plugin.slack.app.AbstractSlackClientTest;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@KestraTest
public class PublishTest extends AbstractSlackClientTest {
    @Inject
    private RunContextFactory runContextFactory;

    @Test
    void run() throws Exception {
        Publish task = Publish.builder()
            .id(IdUtils.create())
            .type(Publish.class.getName())
            .methodsEndpointUrlPrefix(this.client())
            .token(Property.ofValue("token"))
            .userId(Property.ofValue("U1234567890"))
            .hash(Property.ofValue("hash123"))
            .view(Property.ofValue(homeView()))
            .build();

        ViewOutput output = task.run(
            TestsUtils.mockRunContext(runContextFactory, task, Map.of())
        );

        assertThat(output).isNotNull();
        assertThat(output.getViewId()).isEqualTo("V1234567890");
        assertThat(output.getHash()).isEqualTo("hash123");
        assertThat(FakeWebhookController.data).contains("user_id=U1234567890");
        assertThat(FakeWebhookController.data).contains("hash=hash123");
        assertThat(FakeWebhookController.data).contains("view=");
    }

    @Test
    void runWithoutHash() throws Exception {
        Publish task = Publish.builder()
            .id(IdUtils.create())
            .type(Publish.class.getName())
            .methodsEndpointUrlPrefix(this.client())
            .token(Property.ofValue("token"))
            .userId(Property.ofValue("U1234567890"))
            .view(Property.ofValue(homeView()))
            .build();

        ViewOutput output = task.run(
            TestsUtils.mockRunContext(runContextFactory, task, Map.of())
        );

        assertThat(output.getViewId()).isEqualTo("V1234567890");
        assertThat(output.getHash()).isEqualTo("hash123");
        assertThat(FakeWebhookController.data).doesNotContain("hash=");
    }

    @Test
    void failsWhenUserIdIsBlank() {
        Publish task = Publish.builder()
            .id(IdUtils.create())
            .type(Publish.class.getName())
            .methodsEndpointUrlPrefix(this.client())
            .token(Property.ofValue("token"))
            .userId(Property.ofValue("   "))
            .view(Property.ofValue(homeView()))
            .build();

        assertThatThrownBy(
            () -> task.run(
                TestsUtils.mockRunContext(runContextFactory, task, Map.of())
            )
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("'userId' rendered to an empty value");
    }

    @Test
    void failsWhenSlackReturnsError() throws Exception {
        Publish task = Publish.builder()
            .id(IdUtils.create())
            .type(Publish.class.getName())
            .methodsEndpointUrlPrefix(this.client())
            .token(Property.ofValue("token"))
            .userId(Property.ofValue("force_error"))
            .view(Property.ofValue(homeView()))
            .build();

        assertThatThrownBy(
            () -> task.run(
                TestsUtils.mockRunContext(runContextFactory, task, Map.of())
            )
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("invalid_view");
    }

    private static Map<String, Object> homeView() {
        return Map.of(
            "type", "home",
            "blocks", List.of(
                Map.of(
                    "type", "section",
                    "text", Map.of(
                        "type", "mrkdwn",
                        "text", "Test home view"
                    )
                )
            )
        );
    }
}
