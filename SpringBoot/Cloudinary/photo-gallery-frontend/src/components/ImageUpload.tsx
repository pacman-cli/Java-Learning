'use client';

import { useState, useRef } from 'react';
import { Upload, CheckCircle, AlertCircle, Image as ImageIcon } from 'lucide-react';
import toast from 'react-hot-toast';
import { imageApi } from '@/lib/api';
import { Image as ImageType } from '@/types/image';

interface ImageUploadProps {
  onUploadSuccess: (image: ImageType) => void;
}

export default function ImageUpload({ onUploadSuccess }: ImageUploadProps) {
  const [isDragging, setIsDragging] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadStatus, setUploadStatus] = useState<'idle' | 'success' | 'error'>('idle');
  const [errorMessage, setErrorMessage] = useState('');
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
    
    const files = Array.from(e.dataTransfer.files);
    if (files.length > 0) {
      handleFileUpload(files[0]);
    }
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (files && files.length > 0) {
      handleFileUpload(files[0]);
    }
  };

  const handleFileUpload = async (file: File) => {
    // Validate file type
    if (!file.type.startsWith('image/')) {
      setUploadStatus('error');
      setErrorMessage('Please select an image file');
      toast.error('Please select an image file');
      return;
    }

    // Validate file size (10MB limit for images)
    const maxSize = 10 * 1024 * 1024; // 10MB
    if (file.size > maxSize) {
      setUploadStatus('error');
      setErrorMessage('File size must be less than 10MB');
      toast.error('File size must be less than 10MB');
      return;
    }

    setIsUploading(true);
    setUploadStatus('idle');
    setErrorMessage('');

    // Show loading toast
    const loadingToast = toast.loading('Uploading image...');

    try {
      const uploadedImage = await imageApi.uploadImage(file);
      setUploadStatus('success');
      onUploadSuccess(uploadedImage);
      
      // Dismiss loading toast and show success
      toast.dismiss(loadingToast);
      toast.success(`Image "${uploadedImage.name}" uploaded successfully!`, {
        duration: 3000,
      });
    } catch (error: unknown) {
      console.error('Upload error:', error);
      setUploadStatus('error');
      setErrorMessage((error as Error).message || 'Failed to upload image. Please try again.');
      
      // Dismiss loading toast and show error
      toast.dismiss(loadingToast);
      toast.error((error as Error).message || 'Failed to upload image. Please try again.');
    } finally {
      setIsUploading(false);
    }
  };

  const openFileDialog = () => {
    fileInputRef.current?.click();
  };

  const resetUpload = () => {
    setUploadStatus('idle');
    setErrorMessage('');
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div className="w-full max-w-2xl mx-auto">
      <div
        className={`
          relative border-2 border-dashed rounded-lg p-12 text-center cursor-pointer transition-all duration-200
          ${isDragging 
            ? 'border-blue-400 bg-gray-700' 
            : 'border-gray-600 hover:border-blue-500 bg-gray-800 shadow-lg'
          }
          ${isUploading ? 'pointer-events-none opacity-50' : ''}
        `}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
        onClick={openFileDialog}
      >
        <input
          ref={fileInputRef}
          type="file"
          accept="image/*"
          onChange={handleFileSelect}
          className="hidden"
        />

        {isUploading ? (
          <div className="flex flex-col items-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mb-4"></div>
            <p className="text-lg font-medium text-white">Uploading image...</p>
            <p className="text-sm text-gray-300 mt-2">Please wait while we process your image</p>
          </div>
        ) : uploadStatus === 'success' ? (
          <div className="flex flex-col items-center">
            <CheckCircle className="h-16 w-16 text-green-500 mb-4" />
            <p className="text-lg font-medium text-green-400 mb-2">Image uploaded successfully!</p>
            <p className="text-sm text-green-300 mb-4">Your image has been added to the gallery</p>
            <button
              onClick={(e) => {
                e.stopPropagation();
                resetUpload();
              }}
              className="btn-primary-classical"
            >
              Upload Another Image
            </button>
          </div>
        ) : uploadStatus === 'error' ? (
          <div className="flex flex-col items-center">
            <AlertCircle className="h-16 w-16 text-red-500 mb-4" />
            <p className="text-lg font-medium text-red-400 mb-2">Upload failed</p>
            <p className="text-sm text-red-300 mb-4">{errorMessage}</p>
            <button
              onClick={(e) => {
                e.stopPropagation();
                resetUpload();
              }}
              className="btn-danger-classical"
            >
              Try Again
            </button>
          </div>
        ) : (
          <div className="flex flex-col items-center">
            <div className="mb-6">
              <div className="h-20 w-20 bg-gray-700 rounded-lg flex items-center justify-center mx-auto mb-4">
                <ImageIcon className="h-10 w-10 text-gray-300" />
              </div>
              <Upload className="h-8 w-8 text-gray-300 mx-auto mb-4" />
            </div>
            <p className="text-xl font-medium text-white mb-2">
              Drop your images here, or <span className="text-blue-400 underline">browse</span>
            </p>
            <p className="text-gray-300 mb-4">
              Supports JPG, PNG, GIF, WebP up to 10MB
            </p>
            <div className="text-sm text-gray-400">
              <p>• Drag and drop images anywhere on this area</p>
              <p>• Click to browse and select files</p>
            </div>
          </div>
        )}
      </div>

      {/* Success message */}
      {uploadStatus === 'success' && (
        <div className="mt-6 p-4 bg-green-900/20 border border-green-500/30 rounded-lg">
          <div className="flex items-center">
            <CheckCircle className="h-5 w-5 text-green-400 mr-2" />
            <p className="text-sm text-green-300">Image uploaded successfully!</p>
          </div>
        </div>
      )}

      {/* Error message */}
      {uploadStatus === 'error' && (
        <div className="mt-6 p-4 bg-red-900/20 border border-red-500/30 rounded-lg">
          <div className="flex items-center">
            <AlertCircle className="h-5 w-5 text-red-400 mr-2" />
            <p className="text-sm text-red-300">{errorMessage}</p>
          </div>
        </div>
      )}
    </div>
  );
}