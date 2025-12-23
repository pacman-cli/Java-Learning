import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { MagnifyingGlassIcon, ArrowLeftIcon, HomeIcon } from '@heroicons/react/24/outline';

const NotFound: React.FC = () => {
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="mx-auto flex items-center justify-center h-24 w-24 rounded-full bg-gray-100">
          <MagnifyingGlassIcon className="h-12 w-12 text-gray-400" />
        </div>
        <div className="mt-6 text-center">
          <h1 className="text-6xl font-bold text-gray-900">404</h1>
          <h2 className="mt-2 text-3xl font-bold tracking-tight text-gray-900">
            Page Not Found
          </h2>
          <p className="mt-4 text-lg text-gray-600">
            Sorry, we couldn't find the page you're looking for.
          </p>
        </div>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <div className="space-y-4">
            <div className="bg-blue-50 border border-blue-200 rounded-md p-4">
              <div className="flex">
                <div className="ml-3">
                  <h3 className="text-sm font-medium text-blue-800">
                    What happened?
                  </h3>
                  <div className="mt-2 text-sm text-blue-700">
                    <p>
                      The page you requested doesn't exist. It might have been moved, deleted, or you entered the wrong URL.
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-gray-50 rounded-md p-4">
              <h3 className="text-sm font-medium text-gray-900 mb-2">
                Here are some helpful links:
              </h3>
              <ul className="text-sm text-gray-600 space-y-1">
                <li>
                  <Link to="/dashboard" className="text-primary-600 hover:text-primary-500">
                    • Go to Dashboard
                  </Link>
                </li>
                <li>
                  <Link to="/profile" className="text-primary-600 hover:text-primary-500">
                    • View Your Profile
                  </Link>
                </li>
                <li>
                  <Link to="/users" className="text-primary-600 hover:text-primary-500">
                    • Manage Users
                  </Link>
                </li>
                <li>
                  <Link to="/settings" className="text-primary-600 hover:text-primary-500">
                    • Account Settings
                  </Link>
                </li>
              </ul>
            </div>

            <div className="bg-yellow-50 border border-yellow-200 rounded-md p-4">
              <div className="flex">
                <div className="ml-3">
                  <h3 className="text-sm font-medium text-yellow-800">
                    Still can't find what you're looking for?
                  </h3>
                  <div className="mt-2 text-sm text-yellow-700">
                    <p>
                      Try checking the URL for typos or use the search feature on our main pages.
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <div className="flex flex-col space-y-3 sm:flex-row sm:space-y-0 sm:space-x-3">
              <button
                onClick={handleGoBack}
                className="flex-1 inline-flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
              >
                <ArrowLeftIcon className="h-4 w-4 mr-2" />
                Go Back
              </button>

              <Link
                to="/dashboard"
                className="flex-1 inline-flex items-center justify-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
              >
                <HomeIcon className="h-4 w-4 mr-2" />
                Go Home
              </Link>
            </div>

            <div className="text-center">
              <p className="text-xs text-gray-500">
                If you believe this page should exist,{' '}
                <a
                  href="mailto:support@codearena.com"
                  className="font-medium text-primary-600 hover:text-primary-500"
                >
                  let us know
                </a>
                .
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotFound;
