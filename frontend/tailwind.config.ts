import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
      typography: {
        DEFAULT: {
          css: {
            '--tw-prose-invert-body': '#FFFFFF',
            '--tw-prose-invert-bullets': '#FFFFFF',
            'code::before': {
              content: '\u00A0 \u00A0',
            },
            'code::after': {
              content: '\u00A0 \u00A0',
            },
            code: {
              background: 'rgb(0 0 0 / 50%)',
              wordWrap: 'break-word',
              boxDecorationBreak: 'clone',
              padding: '.1rem .3rem .2rem',
              borderRadius: '.2rem',
            }
          }
        }
      }
    },
    colors: {
      background: "#2D2E3A",
      text: "#FEF9EC",
      primary: "#214E73",
      primary_highlight: "#2C7AE9",
      secondary: "#3C5366",
      accent: "#87153A",
      yellow: "#E3EF56",
      green: "5CBC8E",
      error: "#87153A",
      warning: "#E3EF56",
      success: "#5CBC8E",
    },
  },
  plugins: [
    require('@tailwindcss/typography'),
  ],
};
export default config;
