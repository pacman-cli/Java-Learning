vim.api.nvim_create_autocmd({ "VimEnter", "ColorScheme" }, {
	pattern = "*",
	callback = function()
		local groups = {
			"Function", "TSFunction", "@function", "@function.call", "@keyword.return", "TSKeywordReturn"
		}
		for _, group in ipairs(groups) do
			vim.api.nvim_set_hl(0, group, { bold = true })
		end
	end,
})
-- Or remove existing autocmds by their group name (which is prefixed with `lazyvim_` for the defaults)
-- e.g. vim.api.nvim_del_augroup_by_name("lazyvim_wrap_spell")
