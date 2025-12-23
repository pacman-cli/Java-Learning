/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,

  // Disable ESLint during builds (optional - remove if you want strict linting)
  // eslint: {
  //   ignoreDuringBuilds: true,
  // },

  // Disable TypeScript errors during builds (optional - remove for strict type checking)
  // typescript: {
  //   ignoreBuildErrors: false,
  // },

  // Image optimization configuration
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
      },
    ],
  },

  // API rewrites (optional - if you want to proxy API requests)
  // This helps avoid CORS issues during development
  async rewrites() {
    return [
      {
        source: '/api/proxy/:path*',
        destination: 'http://localhost:8080/api/v1/:path*',
      },
    ];
  },

  // Headers configuration for security and CORS
  async headers() {
    return [
      {
        source: '/:path*',
        headers: [
          {
            key: 'X-Frame-Options',
            value: 'DENY',
          },
          {
            key: 'X-Content-Type-Options',
            value: 'nosniff',
          },
          {
            key: 'X-XSS-Protection',
            value: '1; mode=block',
          },
        ],
      },
    ];
  },

  // Experimental features (optional)
  experimental: {
    // Enable server actions if needed
    // serverActions: true,
  },

  // Environment variables validation (optional)
  env: {
    NEXT_PUBLIC_APP_NAME: 'User Management System',
    NEXT_PUBLIC_APP_VERSION: '1.0.0',
  },

  // Webpack configuration (optional)
  webpack: (config, { isServer }) => {
    // Add custom webpack configurations here if needed
    return config;
  },
};

module.exports = nextConfig;
