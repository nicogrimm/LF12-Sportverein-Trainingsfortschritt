all: deps dev
.PHONY: all

deps:
	pnpm install --frozen-lockfile
.PHONY: deps

check:
	pnpm format:check
	pnpm lint
	pnpm svelte-check
.PHONY: check

dev:
	pnpm run dev
.PHONY: dev

build:
	pnpm build
.PHONY: build
