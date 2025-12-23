import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ExclamationTriangleIcon, ArrowLeftIcon, HomeIcon } from '@heroicons/react/24/outline';
import { useAuth } from '../context/AuthContext';

const Unauthorized: React.FC = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="mx-auto flex items-center justify-center h-24 w-24 rounded-full bg-red-100">
          <ExclamationTriangleIcon className="h-12 w-12 text-red-600" />
        </div>
        <div className="mt-6 text-center">
          <h1 className="text-3xl font-bold tracking-tight text-gray-900">
            Access Denied
          </h1>
          <p className="mt-4 text-lg text-gray-600">
            You don't have permission to access this resource.
          </p>
        </div>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <div className="space-y-4">
            <div className="bg-red-50 border border-red-200 rounded-md p-4">
              <div className="flex">
                <div className="flex-shrink-0">
                  <ExclamationTriangleIcon className="h-5 w-5 text-red-400" />
                </div>
                <div className="ml-3">
                  <h3 className="text-sm font-medium text-red-800">
                    Insufficient Permissions
                  </h3>
                  <div className="mt-2 text-sm text-red-700">
                    {isAuthenticated ? (
                      <p>
                        Your current role ({user?.role}) doesn't have the required permissions to view this page.
                        {user?.role === 'USER' && (
                          <span> You may need moderator or administrator privileges.</span>
                        )}
                        {user?.role === 'MODERATOR' && (
                          <span> You may need administrator privileges.</span>
                        )}
                      </p>
                    ) : (
                      <p>You need to be logged in to access this resource.</p>
                    )}
                  </div>
                </div>
              </div>
            </div>

            {isAuthenticated && (
              <div className="bg-blue-50 border border-blue-200 rounded-md p-4">
                <div className="flex">
                  <div className="ml-3">
                    <h3 className="text-sm font-medium text-blue-800">
                      Current Account Status
                    </h3>
                    <div className="mt-2 text-sm text-blue-700">
                      <ul className="list-disc list-inside space-y-1">
                        <li>Username: {user?.username}</li>
                        <li>Role: {user?.role}</li>
                        <li>Status: {user?.isEnabled ? 'Active' : 'Inactive'}</li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            )}

            <div className="bg-gray-50 rounded-md p-4">
              <h3 className="text-sm font-medium text-gray-900 mb-2">
                What you can do:
              </h3>
              <ul className="text-sm text-gray-600 space-y-1">
                {isAuthenticated ? (
                  <>
                    <li>• Contact your administrator to request additional permissions</li>
                    <li>• Return to your dashboard to access available features</li>
                    <li>• Check if you're accessing the correct URL</li>
                  </>
                ) : (
                  <>
                    <li>• Sign in with an account that has the required permissions</li>
                    <li>• Contact support if you believe this is an error</li>
                    <li>• Return to the login page</li>
                  </>
                )}
              </ul>
            </div>

            <div className="flex flex-col space-y-3 sm:flex-row sm:space-y-0 sm:space-x-3">
              <button
                onClick={handleGoBack}
                className="flex-1 inline-flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
              >
                <ArrowLeftIcon className="h-4 w-4 mr-2" />
                Go Back
              </button>

              {isAuthenticated ? (
                <Link
                  to="/dashboard"
                  className="flex-1 inline-flex items-center justify-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                >
                  <HomeIcon className="h-4 w-4 mr-2" />
                  Dashboard
                </Link>
              ) : (
                <Link
                  to="/login"
                  className="flex-1 inline-flex items-center justify-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                >
                  Sign In
                </Link>
              )}
            </div>

            {isAuthenticated && (
              <div className="text-center">
                <p className="text-xs text-gray-500">
                  If you believe you should have access to this resource,{' '}
                  <a
                    href="mailto:support@codearena.com"
                    className="font-medium text-primary-600 hover:text-primary-500"
                  >
                    contact support
                  </a>
                  .
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Unauthorized;
