/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: process.env.MANAGMENT_HOST ? `${process.env.MANAGMENT_HOST}/:path*` : "http://10.43.225.43:8080/:path*",
      },
    ];
  },
};

module.exports = nextConfig;
