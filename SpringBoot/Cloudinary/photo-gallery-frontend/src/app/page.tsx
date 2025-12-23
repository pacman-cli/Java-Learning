'use client';

import { useState } from 'react';
import { Camera, Images, Film } from 'lucide-react';
import ImageUpload from '@/components/ImageUpload';
import ImageGallery from '@/components/ImageGallery';
import VideoUpload from '@/components/VideoUpload';
import VideoGallery from '@/components/VideoGallery';

export default function Home() {
  const [activeTab, setActiveTab] = useState<'upload' | 'gallery'>('upload');
  const [mediaType, setMediaType] = useState<'images' | 'videos'>('images');
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleImageUploadSuccess = () => {
    // Trigger a refresh of the gallery
    setRefreshTrigger(prev => prev + 1);
    // Switch to gallery tab to show the uploaded image
    setActiveTab('gallery');
    setMediaType('images');
  };

  const handleVideoUploadSuccess = () => {
    // Trigger a refresh of the gallery
    setRefreshTrigger(prev => prev + 1);
    // Switch to gallery tab to show the uploaded video
    setActiveTab('gallery');
    setMediaType('videos');
  };

  return (
    <div className="min-h-screen" style={{ backgroundColor: '#0A0F1F' }}>
      {/* Header */}
      <header className="sticky top-0 z-50 bg-gray-800/50 backdrop-blur-md supports-[backdrop-filter]:bg-gray-800/30 border-b border-gray-700 shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-20">
            <div className="flex items-center">
              <div className="flex items-center space-x-3">
                <div className="h-12 w-12 bg-blue-600 rounded-lg flex items-center justify-center shadow-lg">
                  <Camera className="h-7 w-7 text-white" />
                </div>
                <div>
                  <h1 className="text-3xl font-bold text-white">Media Gallery</h1>
                  <p className="text-sm text-gray-300">Dark Collection</p>
                </div>
              </div>
            </div>
            <div className="text-sm text-gray-300/90">
              Powered by Cloudinary & Spring Boot
            </div>
          </div>
        </div>
      </header>
      {/* Spacer to offset sticky header height on small screens */}
      <div className="h-0 sm:h-0"></div>

      {/* Navigation Tabs */}
      <div className="bg-gray-800/60 backdrop-blur-sm border-b border-gray-600">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <nav className="flex space-x-8">
            <button
              onClick={() => setActiveTab('upload')}
              className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                activeTab === 'upload'
                  ? 'border-blue-500 text-white'
                  : 'border-transparent text-gray-400 hover:text-white hover:border-gray-400'
              }`}
            >
              <Camera className="h-4 w-4 inline mr-2" />
              Upload Media
            </button>
            <button
              onClick={() => setActiveTab('gallery')}
              className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                activeTab === 'gallery'
                  ? 'border-blue-500 text-white'
                  : 'border-transparent text-gray-400 hover:text-white hover:border-gray-400'
              }`}
            >
              <Images className="h-4 w-4 inline mr-2" />
              View Gallery
            </button>
          </nav>
        </div>
      </div>

      {/* Media Type Selector */}
      {activeTab === 'upload' && (
        <div className="bg-gray-800/50 border-b border-gray-600">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex space-x-4 py-4">
              <button
                onClick={() => setMediaType('images')}
                className={`flex items-center space-x-2 px-6 py-3 rounded-lg font-medium text-sm transition-colors ${
                  mediaType === 'images'
                    ? 'bg-blue-600 text-white shadow-lg'
                    : 'text-gray-400 hover:text-white hover:bg-gray-700'
                }`}
              >
                <Images className="h-4 w-4" />
                <span>Images</span>
              </button>
              <button
                onClick={() => setMediaType('videos')}
                className={`flex items-center space-x-2 px-6 py-3 rounded-lg font-medium text-sm transition-colors ${
                  mediaType === 'videos'
                    ? 'bg-blue-600 text-white shadow-lg'
                    : 'text-gray-400 hover:text-white hover:bg-gray-700'
                }`}
              >
                <Film className="h-4 w-4" />
                <span>Videos</span>
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {activeTab === 'upload' && (
          <div>
            <div className="text-center mb-12">
              <h2 className="text-4xl font-bold text-white mb-4 font-semibold">
                Upload Your {mediaType === 'images' ? 'Images' : 'Videos'}
              </h2>
              <p className="text-lg text-gray-300 max-w-2xl mx-auto leading-relaxed">
                {mediaType === 'images' 
                  ? 'Drag and drop your images or click to browse. Your photos will be securely stored in Cloudinary and displayed in the gallery.'
                  : 'Drag and drop your videos or click to browse. Your videos will be securely stored in Cloudinary and displayed in the gallery.'
                }
              </p>
            </div>
            {mediaType === 'images' ? (
              <ImageUpload onUploadSuccess={handleImageUploadSuccess} />
            ) : (
              <VideoUpload onUploadSuccess={handleVideoUploadSuccess} />
            )}
          </div>
        )}

        {activeTab === 'gallery' && (
          <div>
            <div className="text-center mb-12">
              <h2 className="text-4xl font-bold text-white mb-4 font-semibold">
                Your {mediaType === 'images' ? 'Image' : 'Video'} Collection
              </h2>
              <p className="text-lg text-gray-300 max-w-2xl mx-auto leading-relaxed">
                Browse and manage your uploaded {mediaType === 'images' ? 'images' : 'videos'} with dark elegance.
              </p>
            </div>
            
            {/* Media Type Toggle for Gallery */}
            <div className="flex justify-center mb-8">
              <div className="bg-gray-800 rounded-lg shadow-lg border border-gray-600 p-1">
                <button
                  onClick={() => setMediaType('images')}
                  className={`flex items-center space-x-2 px-6 py-3 rounded-lg font-medium text-sm transition-colors ${
                    mediaType === 'images'
                      ? 'bg-blue-600 text-white shadow-lg'
                      : 'text-gray-400 hover:text-white'
                  }`}
                >
                  <Images className="h-4 w-4" />
                  <span>Images</span>
                </button>
                <button
                  onClick={() => setMediaType('videos')}
                  className={`flex items-center space-x-2 px-6 py-3 rounded-lg font-medium text-sm transition-colors ${
                    mediaType === 'videos'
                      ? 'bg-blue-600 text-white shadow-lg'
                      : 'text-gray-400 hover:text-white'
                  }`}
                >
                  <Film className="h-4 w-4" />
                  <span>Videos</span>
                </button>
              </div>
            </div>

            {mediaType === 'images' ? (
              <ImageGallery key={`images-${refreshTrigger}`} />
            ) : (
              <VideoGallery key={`videos-${refreshTrigger}`} />
            )}
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="bg-gray-800/50 border-t border-gray-600 mt-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="text-center">
            <div className="mb-4">
              <div className="h-8 w-8 bg-blue-600 rounded-lg flex items-center justify-center mx-auto shadow-lg">
                <Camera className="h-5 w-5 text-white" />
              </div>
            </div>
            <p className="text-gray-300 font-medium mb-2">Built with Next.js, TypeScript, Tailwind CSS, and Spring Boot</p>
            <p className="text-sm text-gray-400">
              Images and videos are stored securely in Cloudinary and metadata in MySQL
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
}