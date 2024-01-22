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
      {
        source: "/login/oauth2/code/google",
        destination: "http://127.0.0.1:8080/login/oauth2/code/google",
      },
    ];
  },
};

module.exports = nextConfig;
