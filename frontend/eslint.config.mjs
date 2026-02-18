// @ts-check
/* eslint-disable @typescript-eslint/no-unsafe-argument, @typescript-eslint/no-unsafe-member-access, @typescript-eslint/no-unsafe-assignment */
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
      "src/lib/components/ui/**",
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
    rules: {
      "@typescript-eslint/no-unsafe-call": "off",
      "@typescript-eslint/no-unsafe-member-access": "off",
      "@typescript-eslint/no-unsafe-return": "off",
      "@typescript-eslint/no-unsafe-assignment": "off",
    },
  },
);
