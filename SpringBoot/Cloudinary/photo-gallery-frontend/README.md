# Photo Gallery Frontend

A modern Next.js frontend application for uploading and viewing photos, integrated with a Spring Boot backend and Cloudinary for image storage.

## Features

- **Photo Upload**: Drag and drop or click to upload images
- **Image Gallery**: View all uploaded photos in a responsive grid layout
- **Full-size View**: Click on any image to view it in full size
- **Download**: Download any image directly from the gallery
- **Responsive Design**: Works on desktop, tablet, and mobile devices
- **Real-time Updates**: Gallery automatically refreshes after uploads

## Tech Stack

- **Frontend**: Next.js 15, TypeScript, Tailwind CSS
- **Icons**: Lucide React
- **HTTP Client**: Axios
- **Backend Integration**: Spring Boot REST API
- **Image Storage**: Cloudinary

## Getting Started

### Prerequisites

- Node.js 18+ 
- npm or yarn
- Running Spring Boot backend (default: http://localhost:8080)

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm run dev
```

3. Open [http://localhost:3000](http://localhost:3000) in your browser

### Backend Configuration

Make sure your Spring Boot backend is running on `http://localhost:8080` with the following endpoints:

- `POST /api/images/upload` - Upload image
- `GET /api/images` - Get all images

If your backend runs on a different port, update the `API_BASE_URL` in `src/lib/api.ts`.

## Project Structure

```
src/
├── app/
│   ├── layout.tsx          # Root layout
│   ├── page.tsx            # Main page
│   └── globals.css         # Global styles
├── components/
│   ├── ImageUpload.tsx     # Upload component
│   └── ImageGallery.tsx    # Gallery component
├── lib/
│   └── api.ts              # API client
└── types/
    └── image.ts            # TypeScript types
```

## API Integration

The frontend communicates with the Spring Boot backend through:

- **Upload**: Sends multipart form data to `/api/images/upload`
- **Gallery**: Fetches image list from `/api/images`

## Features in Detail

### Upload Component
- Drag and drop support
- File type validation (images only)
- File size validation (100MB limit)
- Upload progress indication
- Error handling and user feedback

### Gallery Component
- Responsive grid layout
- Lazy loading for performance
- Hover effects and actions
- Modal for full-size viewing
- Download functionality

## Styling

The application uses Tailwind CSS for styling with:
- Modern, clean design
- Responsive layout
- Smooth animations and transitions
- Accessible color scheme
- Mobile-first approach

## Development

### Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run start` - Start production server
- `npm run lint` - Run ESLint

### Customization

- Update colors in Tailwind config
- Modify API endpoints in `src/lib/api.ts`
- Add new features in components
- Extend TypeScript types as needed

## Deployment

1. Build the application:
```bash
npm run build
```

2. Deploy the `out` folder to your hosting service (Vercel, Netlify, etc.)

3. Ensure your backend is accessible from the deployed frontend

## Troubleshooting

### Common Issues

1. **CORS Errors**: Make sure your Spring Boot backend has CORS enabled
2. **Upload Failures**: Check file size limits and network connectivity
3. **Images Not Loading**: Verify Cloudinary URLs and backend connectivity

### Backend Requirements

- CORS enabled for `http://localhost:3000`
- File upload size limits configured
- Cloudinary credentials properly set
- MySQL database running and accessible