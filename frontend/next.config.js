/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: `${process.env.MANAGMENT_HOST}/:path*` ?? "http://127.0.0.1:8080/:path*",
      },
    ];
  },
};

module.exports = nextConfig;
