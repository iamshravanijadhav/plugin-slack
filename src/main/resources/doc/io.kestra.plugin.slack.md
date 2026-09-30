# How to use the Slack plugin

Choose between an incoming webhook URL for simple notifications and a Slack App bot token for the full API surface.

## Authentication

**Incoming webhook** (`SlackIncomingWebhook`, `SlackExecution`): set the `url` property to a Slack incoming webhook URL. Create one at [api.slack.com/messaging/webhooks](https://api.slack.com/messaging/webhooks).

**Bot token** (`app.chats.Post` and other app tasks): set the `token` property to a Slack bot token with the `chat:write` scope. Create a Slack app at [api.slack.com/apps](https://api.slack.com/apps).

Store both in [secrets](https://kestra.io/docs/concepts/secret).

## Tasks

For in-flow notifications, use `SlackIncomingWebhook` with `messageText` for Slack-formatted text or `payload` for a custom JSON body. For automated failure monitoring, `SlackExecution` sends a structured alert — including an execution link, status, and duration — and is designed to be used with a [Flow trigger](https://kestra.io/docs/workflow-components/triggers) in a dedicated monitoring namespace that watches other namespaces for failures rather than adding error handling to individual flows. Bot token tasks under `app.chats`, `app.conversations`, `app.files`, `app.reactions`, and `app.views` cover the full Slack API surface for thread replies, file uploads, channel management, and more. The `app.views.Open` task opens a Slack modal using a `triggerId` from an interaction payload; the `triggerId` expires after about 3 seconds. The `app.views.Publish` task publishes a Home tab view and requires Slack App Home to be enabled.
