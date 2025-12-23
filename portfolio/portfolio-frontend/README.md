# Portfolio Website - Frontend

A cutting-edge, professional portfolio website built with Next.js, Tailwind CSS, and Framer Motion.

## Features

- 🌗 Dark/Light mode toggle
- 🎨 Modern UI with gradient accents and smooth animations
- 📱 Fully responsive design
- 🚀 Optimized performance with Next.js
- 🎭 Interactive components with Framer Motion
- 📊 Animated skill bars
- 🗂️ Filterable project showcase
- 📅 Experience timeline
- 📞 Contact form with validation
- 🔧 Mobile-friendly navigation

## Tech Stack

- **Next.js 14** - React framework for production
- **Tailwind CSS** - Utility-first CSS framework
- **Framer Motion** - Animation library for React
- **AOS (Animate On Scroll)** - Scroll animations
- **React Icons** - Popular icon library

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

1. Clone the repository:

```bash
git clone <repository-url>
```

2. Navigate to the frontend directory:

```bash
cd portfolio-frontend
```

3. Install dependencies:

```bash
npm install
```

### Development

Run the development server:

```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) to view the application.

### Build

Create a production build:

```bash
npm run build
```

### Deployment

Deploy to Vercel or any static hosting service:

```bash
npm run start
```

## Project Structure

```
portfolio-frontend/
├── public/                 # Static assets
├── src/
│   ├── app/                # Next.js app directory
│   │   ├── layout.js       # Root layout
│   │   └── page.js         # Main page
│   ├── components/         # Reusable components
│   │   ├── Skills.js
│   │   ├── Projects.js
│   │   ├── Experience.js
│   │   ├── Education.js
│   │   ├── Contact.js
│   │   └── Footer.js
│   ├── styles/             # Global styles
│   └── utils/              # Utility functions
├── tailwind.config.js      # Tailwind CSS configuration
├── postcss.config.js       # PostCSS configuration
└── next.config.js          # Next.js configuration
```

## Customization

1. Update personal information in components
2. Modify color scheme in `tailwind.config.js` and `src/app/globals.css`
3. Add your own projects in `src/components/Projects.js`
4. Customize animations in component files

## Dependencies

```json
{
  "dependencies": {
    "next": "14.2.15",
    "react": "18.3.1",
    "react-dom": "18.3.1",
    "framer-motion": "^11.0.0",
    "aos": "^2.3.4",
    "react-icons": "^5.0.0"
  },
  "devDependencies": {
    "autoprefixer": "^10.4.16",
    "postcss": "^8.4.32",
    "tailwindcss": "^3.3.6",
    "eslint": "^8.56.0",
    "eslint-config-next": "14.2.15"
  }
}
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a pull request

## License

This project is licensed under the MIT License.
