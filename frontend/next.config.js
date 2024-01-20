/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://127.0.0.1:8080/:path*",
      },
      {
        source: "/login/oauth2/code/github",
        destination: "http://127.0.0.1:8080/login/oauth2/code/github",
      },
    ];
  },
};

module.exports = nextConfig;
