import axios from 'axios';
import { Image } from '@/types/image';
import { Video } from '@/types/video';

// Prefer env BACKEND_URL; fallback to Next.js dev proxy (/api) to avoid CORS in dev
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || '/api';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
  withCredentials: false,
});

export const imageApi = {
  // Upload a single image
  uploadImage: async (file: File): Promise<Image> => {
    const formData = new FormData();
    formData.append('file', file);
    
    console.log('Uploading image:', file.name);
    
    const response = await api.post('/images/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    console.log('Upload response:', response.data);
    return response.data;
  },

  // Get all images
  getAllImages: async (): Promise<Image[]> => {
    console.log('Fetching images from:', API_BASE_URL);
    
    try {
      // Try native fetch first
      const response = await fetch(`${API_BASE_URL}/images`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        mode: 'cors',
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      console.log('Images response:', data);
      return data;
    } catch (error) {
      console.error('Error fetching images:', error);
      throw error;
    }
  },

  // Delete an image
  deleteImage: async (publicId: string): Promise<void> => {
    console.log('Deleting image:', publicId);
    
    try {
      const response = await fetch(`${API_BASE_URL}/images/${publicId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        mode: 'cors',
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      console.log('Image deleted successfully');
    } catch (error) {
      console.error('Error deleting image:', error);
      throw error;
    }
  },
};

export const videoApi = {
  // Upload a single video
  uploadVideo: async (file: File): Promise<Video> => {
    const formData = new FormData();
    formData.append('file', file);
    
    console.log('Uploading video:', file.name);
    
    try {
      const response = await api.post('/videos/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      
      console.log('Upload response:', response.data);
      return response.data;
    } catch (error: unknown) {
      console.error('Video upload error:', error);
      
      // Extract error message from response
      let errorMessage = 'Failed to upload video. Please try again.';
      
      if ((error as { response?: { data?: { error?: string } } }).response?.data?.error) {
        errorMessage = (error as { response: { data: { error: string } } }).response.data.error;
      } else if ((error as Error).message) {
        errorMessage = (error as Error).message;
      }
      
      throw new Error(errorMessage);
    }
  },

  // Get all videos
  getAllVideos: async (): Promise<Video[]> => {
    console.log('Fetching videos from:', API_BASE_URL);
    
    try {
      // Try native fetch first
      const response = await fetch(`${API_BASE_URL}/videos`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        mode: 'cors',
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      console.log('Videos response:', data);
      return data;
    } catch (error) {
      console.error('Error fetching videos:', error);
      throw error;
    }
  },

  // Delete a video
  deleteVideo: async (publicId: string): Promise<void> => {
    console.log('Deleting video:', publicId);
    
    try {
      const response = await fetch(`${API_BASE_URL}/videos/${publicId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        mode: 'cors',
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      console.log('Video deleted successfully');
    } catch (error) {
      console.error('Error deleting video:', error);
      throw error;
    }
  },
};
