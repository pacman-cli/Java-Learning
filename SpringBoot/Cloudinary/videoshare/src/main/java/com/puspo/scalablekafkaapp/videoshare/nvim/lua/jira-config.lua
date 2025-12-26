-- Jira Configuration
-- Copy this file and rename to jira-config.lua
-- Fill in your Jira credentials and uncomment the lines

return {
  -- REQUIRED: Your Jira instance URL (without trailing slash)
  jira_url = "https://your-company.atlassian.net",

  -- REQUIRED: Your Jira username/email
  username = "your-email@company.com",

  -- REQUIRED: Your Jira API token (generate at: https://id.atlassian.com/manage-profile/security/api-tokens)
  api_token = "your-api-token-here",

  -- OPTIONAL: Default project key
  default_project = "PROJECT",

  -- OPTIONAL: Default issue type
  default_issue_type = "Task",

  -- OPTIONAL: Custom fields mapping
  custom_fields = {},

  -- OPTIONAL: JQL queries for quick access
  saved_queries = {
    my_issues = "assignee = currentUser() AND status != Done",
    recent_issues = "updated >= -7d ORDER BY updated DESC",
    in_progress = "status = 'In Progress' AND assignee = currentUser()",
  },
}
