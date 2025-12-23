'use client';

import { useState, useEffect } from 'react';
import { Video as VideoIcon, Download, Loader2, Play, Trash2, X } from 'lucide-react';
import toast from 'react-hot-toast';
import { videoApi } from '@/lib/api';
import { Video as VideoType } from '@/types/video';
import VideoPlayer from './VideoPlayer';

export default function VideoGallery() {
  const [videos, setVideos] = useState<VideoType[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedVideo, setSelectedVideo] = useState<VideoType | null>(null);

  useEffect(() => {
    fetchVideos();
  }, []);

  const fetchVideos = async () => {
    try {
      setLoading(true);
      const fetchedVideos = await videoApi.getAllVideos();
      setVideos(fetchedVideos);
    } catch {
      toast.error('Failed to load videos. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && selectedVideo) {
        closeModal();
      }
    };

    if (selectedVideo) {
      document.addEventListener('keydown', handleKeyDown);
      document.body.style.overflow = 'hidden';
    }

    return () => {
      document.removeEventListener('keydown', handleKeyDown);
      document.body.style.overflow = 'unset';
    };
  }, [selectedVideo]);

  const handleDownload = (video: VideoType) => {
    try {
      const link = document.createElement('a');
      link.href = video.url;
      link.download = video.name;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      
      toast.success(`Downloading "${video.name}"`, {
        duration: 2000,
      });
    } catch (_error) {
      toast.error('Failed to download video');
    }
  };

  const handleDelete = async (video: VideoType) => {
    if (!confirm(`Are you sure you want to delete "${video.name}"? This action cannot be undone.`)) {
      return;
    }

    try {
      await videoApi.deleteVideo(video.publicId);
      toast.success(`"${video.name}" deleted successfully`, {
        duration: 3000,
      });
      
      // Refresh the gallery
      fetchVideos();
    } catch (error) {
      console.error('Error deleting video:', error);
      toast.error('Failed to delete video. Please try again.');
    }
  };

  const openModal = (video: VideoType) => {
    setSelectedVideo(video);
  };

  const closeModal = () => {
    setSelectedVideo(null);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <div className="text-center">
          <Loader2 className="h-12 w-12 text-amber-600 animate-spin mx-auto mb-4" />
          <p className="text-amber-700 font-medium">Loading videos...</p>
        </div>
      </div>
    );
  }

  if (videos.length === 0) {
    return (
      <div className="text-center py-12">
        <div className="bg-white rounded-sm shadow-sm border border-amber-200 p-12 max-w-md mx-auto">
          <VideoIcon className="h-16 w-16 text-amber-400 mx-auto mb-6" />
          <h3 className="text-xl font-serif text-amber-900 mb-3 font-semibold">No videos yet</h3>
          <p className="text-amber-600 mb-6">Upload your first video to get started!</p>
          <div className="text-sm text-amber-500">
            <p>• Supported formats: MP4, MOV, AVI, WebM</p>
            <p>• Maximum file size: 500MB</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {videos.map((video) => (
          <div
            key={video.id}
            className="group relative bg-white rounded-sm shadow-sm overflow-hidden hover:shadow-md transition-all duration-200 cursor-pointer border border-amber-200"
            onClick={() => openModal(video)}
          >
            <div className="aspect-video relative overflow-hidden bg-amber-50">
              <video
                src={video.url}
                className="w-full h-full object-cover"
                preload="none"
                onMouseEnter={(e) => {
                  // Start loading when user hovers
                  const videoElement = e.target as HTMLVideoElement;
                  videoElement.load();
                }}
              />
              
              {/* Play button overlay */}
              <div className="absolute inset-0 flex items-center justify-center bg-black/20 group-hover:bg-black/30 transition-all duration-200">
                <div className="bg-white/90 rounded-full p-3 group-hover:bg-white transition-all duration-200">
                  <Play className="h-6 w-6 text-gray-800 ml-1" fill="currentColor" />
                </div>
              </div>
              
              {/* Action buttons at bottom */}
              <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-3 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                <div className="flex space-x-2">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleDownload(video);
                    }}
                    className="flex-1 flex items-center justify-center space-x-2 bg-white/90 hover:bg-white text-gray-800 py-2 px-3 rounded-sm transition-colors duration-200"
                    title="Download video"
                  >
                    <Download className="h-4 w-4" />
                    <span className="text-sm font-medium">Download</span>
                  </button>
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleDelete(video);
                    }}
                    className="flex items-center justify-center space-x-2 bg-red-600 hover:bg-red-700 text-white py-2 px-3 rounded-sm transition-colors duration-200"
                    title="Delete video"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              </div>
            </div>
            
            <div className="p-4">
              <p className="text-sm font-medium text-amber-900 truncate" title={video.name}>
                {video.name}
              </p>
            </div>
          </div>
        ))}
      </div>

      {/* Full-screen video modal */}
      {selectedVideo && (
        <div 
          className="modal-classical"
          onClick={closeModal}
        >
          <div 
            className="modal-content-classical"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="flex justify-between items-center p-4 border-b border-amber-200">
              <h3 className="text-lg font-serif text-amber-900 font-semibold">
                {selectedVideo.name}
              </h3>
              <button
                onClick={closeModal}
                className="text-amber-600 hover:text-amber-800 transition-colors"
              >
                <X className="h-6 w-6" />
              </button>
            </div>
            <div className="p-4">
              <VideoPlayer video={selectedVideo} />
            </div>
            <div className="flex justify-center space-x-4 p-4 border-t border-amber-200">
              <button
                onClick={() => handleDownload(selectedVideo)}
                className="btn-primary-classical"
              >
                <Download className="h-4 w-4 mr-2" />
                Download
              </button>
              <button
                onClick={() => {
                  handleDelete(selectedVideo);
                  closeModal();
                }}
                className="btn-danger-classical"
              >
                <Trash2 className="h-4 w-4 mr-2" />
                Delete
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}