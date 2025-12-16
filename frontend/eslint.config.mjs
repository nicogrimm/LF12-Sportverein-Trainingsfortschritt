// @ts-check
import globals from "globals";
import { defineConfig } from "eslint/config";
import eslint from "@eslint/js";
import tseslint from "typescript-eslint";
import eslintPluginSvelte from "eslint-plugin-svelte";
import eslintConfigPrettier from "eslint-config-prettier";
import tsParser from "@typescript-eslint/parser";
import svelteParser from "svelte-eslint-parser";
import svelteConfig from "./svelte.config.js";

export default defineConfig(
  eslint.configs.recommended,
  tseslint.configs.recommendedTypeChecked,
  eslintPluginSvelte.configs["flat/recommended"],
  eslintConfigPrettier,
  {
    files: ["**/*.{js,mjs,cjs,ts,svelte}"],
  },
  {
    ignores: [
      "node_modules",
      "coverage",
      "dist",
      "config/**/*.js",
      ".github",
      ".yarn",
    ],
  },
  {
    languageOptions: {
      globals: {
        ...globals.browser,
      },
      parserOptions: {
        projectService: {
          allowDefaultProject: ["eslint.config.mjs", "svelte.config.js"],
        },
        tsconfigRootDir: import.meta.dirname,
        extraFileExtensions: [".svelte"],
      },
    },
  },
  {
    files: ["**/*.svelte"],
    languageOptions: {
      parser: svelteParser,
      parserOptions: {
        parser: tsParser,
        svelteConfig,
      },
    },
  },
);
