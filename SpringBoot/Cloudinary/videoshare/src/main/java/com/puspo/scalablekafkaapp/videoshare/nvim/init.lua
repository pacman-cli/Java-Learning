require("config.lazy")
vim.schedule(function()
	local groups = {
	"Function", "TSFunction", "@function", "@function.call", "@keyword.return", "TSKeywordReturn",
	"@keyword.import", "TSKeywordImport", "Import"
	}
		for _, group in ipairs(groups) do
			local ok, current = pcall(vim.api.nvim_get_hl_by_name, group, true)
			if ok then
				vim.api.nvim_set_hl(0, group, vim.tbl_extend("force", current, { bold = true, default = false }))
			end
		end
end)

vim.schedule(function()
	local variable_groups = {
		"@variable", "TSVariable", "Identifier"
	}
	for _, group in ipairs(variable_groups) do
		vim.api.nvim_set_hl(0, group, { fg = "#8c977d", bold = true })
	end
end)

vim.api.nvim_create_autocmd("ColorScheme", {
	callback = function()
		local variable_groups = { "@variable", "TSVariable", "Identifier" }
		for _, group in ipairs(variable_groups) do
			vim.api.nvim_set_hl(0, group, { fg = "#8c977d", bold = true })
		end
	end,
})

-- Transparency settings for Ghosty terminal compatibility
vim.api.nvim_create_autocmd("ColorScheme", {
  callback = function()
    -- Make background transparent
    vim.api.nvim_set_hl(0, "Normal", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "NormalFloat", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "NormalNC", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "SignColumn", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "LineNr", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "Folded", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "NonText", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "SpecialKey", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "VertSplit", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "WinSeparator", { bg = "NONE" })
    vim.api.nvim_set_hl(0, "EndOfBuffer", { bg = "NONE" })
  end,
})

-- Apply transparency immediately
vim.schedule(function()
  vim.api.nvim_set_hl(0, "Normal", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "NormalFloat", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "NormalNC", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "SignColumn", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "LineNr", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "Folded", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "NonText", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "SpecialKey", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "VertSplit", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "WinSeparator", { bg = "NONE" })
  vim.api.nvim_set_hl(0, "EndOfBuffer", { bg = "NONE" })
end)
