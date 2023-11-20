import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
        'gradient-conic':
          'conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))',
      },
    },
    colors: {
        background: '#2D2E3A',
        text: '#FEF9EC',
        primary: '#214E73',
        primary_highlight: '#2C7AE9',
        secondary: '#3C5366',
        accent: '#87153A',
        yellow: '#E3EF56',
        green: '5CBC8E',
    }
  },
  plugins: [],
}
export default config
