/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: process.env.MANAGMENT_HOST ? `${process.env.MANAGMENT_HOST}/:path*` : "http://23ws-ase-pr-inso-04.apps.student.inso-w.at/management/:path*",
      },
    ];
  },
};

module.exports = nextConfig;
