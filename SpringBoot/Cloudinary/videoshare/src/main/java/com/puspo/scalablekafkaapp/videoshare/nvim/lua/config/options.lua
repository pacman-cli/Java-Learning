-- Options are automatically loaded before lazy.nvim startup
-- Default options that are always set: https://github.com/LazyVim/LazyVim/blob/main/lua/lazyvim/config/options.lua
-- Add any additional options here

-- Enable word wrap
vim.opt.wrap = true
vim.opt.linebreak = true -- Break at word boundaries
vim.opt.breakindent = true -- Indent wrapped lines to match the start

-- Show wrap symbols
vim.opt.showbreak = "⤷ " -- Symbol to show at the beginning of wrapped lines
vim.opt.listchars = {
  tab = "→ ",
  trail = "•",
  extends = "»",
  precedes = "«",
  nbsp = "␣"
}
vim.opt.list = true -- Enable showing listchars

-- Transparency settings for Ghosty terminal
vim.opt.termguicolors = true -- Enable 24-bit RGB colors
vim.opt.winblend = 0 -- Window transparency (0-100, 0 = opaque)
vim.opt.pumblend = 0 -- Popup menu transparency
