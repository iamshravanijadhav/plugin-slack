# Kestra Slack Plugin

## What

- Provides plugin components under `io.kestra.plugin.slack`.
- Includes classes such as `SlackTemplate`, `SlackIncomingWebhook`, `SlackExecution`, `MessageService`.

## Why

- What user problem does this solve? Teams need to slack notification webhooks and full Slack App automations across chat, files, canvases, channels, reactions, users, and events from orchestrated workflows instead of relying on manual console work, ad hoc scripts, or disconnected schedulers.
- Why would a team adopt this plugin in a workflow? It keeps Slack steps in the same Kestra flow as upstream preparation, approvals, retries, notifications, and downstream systems.
- What operational/business outcome does it enable? It reduces manual handoffs and fragmented tooling while improving reliability, traceability, and delivery speed for processes that depend on Slack.

## How

### Architecture

Single-module plugin. Source packages under `io.kestra.plugin`:

- `slack`

Infrastructure dependencies (Docker Compose services):

- `app`

### Key Plugin Classes

- `io.kestra.plugin.slack.app.canvases.AccessSet`
- `io.kestra.plugin.slack.app.canvases.Create`
- `io.kestra.plugin.slack.app.canvases.Delete`
- `io.kestra.plugin.slack.app.canvases.Edit`
- `io.kestra.plugin.slack.app.canvases.SectionsLookup`
- `io.kestra.plugin.slack.app.chats.AppendStream`
- `io.kestra.plugin.slack.app.chats.Delete`
- `io.kestra.plugin.slack.app.chats.DeleteScheduled`
- `io.kestra.plugin.slack.app.chats.Post`
- `io.kestra.plugin.slack.app.chats.PostEphemeral`
- `io.kestra.plugin.slack.app.chats.Schedule`
- `io.kestra.plugin.slack.app.chats.StartStream`
- `io.kestra.plugin.slack.app.chats.StopStream`
- `io.kestra.plugin.slack.app.chats.Update`
- `io.kestra.plugin.slack.app.conversations.Archive`
- `io.kestra.plugin.slack.app.conversations.Close`
- `io.kestra.plugin.slack.app.conversations.Create`
- `io.kestra.plugin.slack.app.conversations.History`
- `io.kestra.plugin.slack.app.conversations.Info`
- `io.kestra.plugin.slack.app.conversations.Invite`
- `io.kestra.plugin.slack.app.conversations.Join`
- `io.kestra.plugin.slack.app.conversations.Kick`
- `io.kestra.plugin.slack.app.conversations.Leave`
- `io.kestra.plugin.slack.app.conversations.List`
- `io.kestra.plugin.slack.app.conversations.Members`
- `io.kestra.plugin.slack.app.conversations.Open`
- `io.kestra.plugin.slack.app.conversations.Rename`
- `io.kestra.plugin.slack.app.conversations.SetPurpose`
- `io.kestra.plugin.slack.app.conversations.SetTopic`
- `io.kestra.plugin.slack.app.conversations.Unarchive`
- `io.kestra.plugin.slack.app.core.Trigger`
- `io.kestra.plugin.slack.app.files.Delete`
- `io.kestra.plugin.slack.app.files.Info`
- `io.kestra.plugin.slack.app.files.Upload`
- `io.kestra.plugin.slack.app.reactions.Add`
- `io.kestra.plugin.slack.app.reactions.Get`
- `io.kestra.plugin.slack.app.reactions.Remove`
- `io.kestra.plugin.slack.app.users.Conversations`
- `io.kestra.plugin.slack.app.users.GetPresence`
- `io.kestra.plugin.slack.app.users.Info`
- `io.kestra.plugin.slack.app.users.List`
- `io.kestra.plugin.slack.app.users.LookupByEmail`
- `io.kestra.plugin.slack.app.users.ProfileGet`
- `io.kestra.plugin.slack.app.views.Open`
- `io.kestra.plugin.slack.app.views.Publish`
- `io.kestra.plugin.slack.notifications.SlackExecution`
- `io.kestra.plugin.slack.notifications.SlackIncomingWebhook`

### Project Structure

```
plugin-slack/
├── src/main/java/io/kestra/plugin/slack/services/
├── src/test/java/io/kestra/plugin/slack/services/
├── build.gradle
└── README.md
```

## References

- https://kestra.io/docs/plugin-developer-guide
- https://kestra.io/docs/plugin-developer-guide/contribution-guidelines
