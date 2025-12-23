'use client';

import { useState, useEffect } from 'react';
import { Image as ImageIcon, Download, Loader2, X, Trash2 } from 'lucide-react';
import toast from 'react-hot-toast';
import { imageApi } from '@/lib/api';
import { Image as ImageType } from '@/types/image';

export default function ImageGallery() {
  const [images, setImages] = useState<ImageType[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedImage, setSelectedImage] = useState<ImageType | null>(null);

  useEffect(() => {
    fetchImages();
  }, []);

  const fetchImages = async () => {
    try {
      setLoading(true);
      const fetchedImages = await imageApi.getAllImages();
      setImages(fetchedImages);
    } catch {
      toast.error('Failed to load images. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && selectedImage) {
        closeModal();
      }
    };

    if (selectedImage) {
      document.addEventListener('keydown', handleKeyDown);
      document.body.style.overflow = 'hidden';
    }

    return () => {
      document.removeEventListener('keydown', handleKeyDown);
      document.body.style.overflow = 'unset';
    };
  }, [selectedImage]);

  const handleDownload = (image: ImageType) => {
    try {
      const link = document.createElement('a');
      link.href = image.url;
      link.download = image.name;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      
      toast.success(`Downloading "${image.name}"`, {
        duration: 2000,
      });
    } catch (error) {
      toast.error('Failed to download image');
    }
  };

  const handleDelete = async (image: ImageType) => {
    if (!confirm(`Are you sure you want to delete "${image.name}"? This action cannot be undone.`)) {
      return;
    }

    try {
      await imageApi.deleteImage(image.publicId);
      toast.success(`"${image.name}" deleted successfully`, {
        duration: 3000,
      });
      
      // Refresh the gallery
      fetchImages();
    } catch (error) {
      console.error('Error deleting image:', error);
      toast.error('Failed to delete image. Please try again.');
    }
  };

  const openModal = (image: ImageType) => {
    setSelectedImage(image);
  };

  const closeModal = () => {
    setSelectedImage(null);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <div className="text-center">
          <Loader2 className="h-12 w-12 text-amber-600 animate-spin mx-auto mb-4" />
          <p className="text-amber-700 font-medium">Loading images...</p>
        </div>
      </div>
    );
  }

  if (images.length === 0) {
    return (
      <div className="text-center py-12">
        <div className="bg-gray-800 rounded-lg shadow-lg border border-gray-600 p-12 max-w-md mx-auto">
          <ImageIcon className="h-16 w-16 text-blue-400 mx-auto mb-6" />
          <h3 className="text-xl font-bold text-white mb-3 font-semibold">No images yet</h3>
          <p className="text-gray-300 mb-6">Upload your first image to get started!</p>
          <div className="text-sm text-gray-400">
            <p>• Supported formats: JPG, PNG, GIF, WebP</p>
            <p>• Maximum file size: 10MB</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {images.map((image) => (
          <div
            key={image.id}
            className="group relative bg-gray-800 rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition-all duration-200 cursor-pointer border border-gray-600"
            onClick={() => openModal(image)}
          >
            <div className="aspect-square relative overflow-hidden">
              <img
                src={image.url}
                alt={image.name}
                className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-200"
                loading="lazy"
              />
              
              {/* Action buttons at bottom */}
              <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-3 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                <div className="flex space-x-2">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleDownload(image);
                    }}
                    className="flex-1 flex items-center justify-center space-x-2 bg-gray-700/90 hover:bg-gray-700 text-white py-2 px-3 rounded-lg transition-colors duration-200"
                    title="Download image"
                  >
                    <Download className="h-4 w-4" />
                    <span className="text-sm font-medium">Download</span>
                  </button>
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleDelete(image);
                    }}
                    className="flex items-center justify-center space-x-2 bg-red-600 hover:bg-red-700 text-white py-2 px-3 rounded-lg transition-colors duration-200"
                    title="Delete image"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              </div>
            </div>
            
            <div className="p-4">
              <p className="text-sm font-medium text-white truncate" title={image.name}>
                {image.name}
              </p>
            </div>
          </div>
        ))}
      </div>

      {/* Full-screen modal */}
      {selectedImage && (
        <div 
          className="modal-classical"
          onClick={closeModal}
        >
          <div 
            className="modal-content-classical"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="flex justify-between items-center p-4 border-b border-gray-600">
              <h3 className="text-lg font-bold text-white font-semibold">
                {selectedImage.name}
              </h3>
              <button
                onClick={closeModal}
                className="text-gray-400 hover:text-white transition-colors"
              >
                <X className="h-6 w-6" />
              </button>
            </div>
            <div className="p-4">
              <img
                src={selectedImage.url}
                alt={selectedImage.name}
                className="max-w-full max-h-[70vh] object-contain mx-auto"
              />
            </div>
            <div className="flex justify-center space-x-4 p-4 border-t border-gray-600">
              <button
                onClick={() => handleDownload(selectedImage)}
                className="btn-primary-classical"
              >
                <Download className="h-4 w-4 mr-2" />
                Download
              </button>
              <button
                onClick={() => {
                  handleDelete(selectedImage);
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