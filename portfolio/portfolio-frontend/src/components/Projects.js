"use client";

import { useState } from "react";
import { motion } from "framer-motion";
import { FiGithub, FiExternalLink, FiStar } from "react-icons/fi";

const projectsData = [
  {
    id: 1,
    title: "E-Commerce Platform",
    description:
      "Full-stack e-commerce solution with React, Node.js, and MongoDB. Features include user authentication, payment processing, and admin dashboard.",
    image: "/projects/ecommerce.jpg",
    technologies: ["React", "Node.js", "MongoDB", "Stripe"],
    github: "#",
    live: "#",
    featured: true,
  },
  {
    id: 2,
    title: "Task Management App",
    description:
      "Collaborative task management application with real-time updates, drag-and-drop interface, and team collaboration features.",
    image: "/projects/taskmanager.jpg",
    technologies: ["Next.js", "Firebase", "Tailwind CSS"],
    github: "#",
    live: "#",
    featured: true,
  },
  {
    id: 3,
    title: "AI Image Generator",
    description:
      "Web application that generates images from text prompts using OpenAI's DALL-E API with user gallery and sharing features.",
    image: "/projects/aigenerator.jpg",
    technologies: ["React", "OpenAI API", "Node.js"],
    github: "#",
    live: "#",
    featured: false,
  },
  {
    id: 4,
    title: "Portfolio Website",
    description:
      "Modern portfolio website with 3D animations, dark mode, and responsive design built with Three.js and Framer Motion.",
    image: "/projects/portfolio.jpg",
    technologies: ["Next.js", "Three.js", "Framer Motion"],
    github: "#",
    live: "#",
    featured: false,
  },
  {
    id: 5,
    title: "Weather Dashboard",
    description:
      "Real-time weather dashboard with forecasting, map integration, and location-based services using OpenWeatherMap API.",
    image: "/projects/weather.jpg",
    technologies: ["React", "OpenWeatherMap API", "Chart.js"],
    github: "#",
    live: "#",
    featured: false,
  },
  {
    id: 6,
    title: "Social Media Analytics",
    description:
      "Analytics platform for social media metrics with data visualization, reporting, and competitor analysis features.",
    image: "/projects/analytics.jpg",
    technologies: ["Python", "Django", "D3.js", "PostgreSQL"],
    github: "#",
    live: "#",
    featured: false,
  },
];

export default function Projects() {
  const [filter, setFilter] = useState("All");
  const [hoveredProject, setHoveredProject] = useState(null);

  const categories = ["All", "Featured", "Frontend", "Backend", "Fullstack"];

  const filteredProjects =
    filter === "All"
      ? projectsData
      : filter === "Featured"
      ? projectsData.filter((project) => project.featured)
      : projectsData;

  return (
    <section id="projects" className="py-20 px-4">
      <div className="max-w-6xl mx-auto">
        <motion.h2
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.5 }}
          className="text-3xl md:text-4xl font-bold text-center mb-16"
        >
          My{" "}
          <span className="bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent">
            Projects
          </span>
        </motion.h2>

        {/* Filter Buttons */}
        <div className="flex flex-wrap justify-center gap-4 mb-12">
          {categories.map((category) => (
            <button
              key={category}
              onClick={() => setFilter(category)}
              className={`px-6 py-2 rounded-full transition-all ${
                filter === category
                  ? "bg-gradient-to-r from-purple-600 to-pink-600 text-white"
                  : "bg-gray-800/50 text-gray-300 hover:bg-gray-700/50"
              }`}
            >
              {category}
            </button>
          ))}
        </div>

        {/* Projects Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {filteredProjects.map((project, index) => (
            <motion.div
              key={project.id}
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
              onMouseEnter={() => setHoveredProject(project.id)}
              onMouseLeave={() => setHoveredProject(null)}
              className="bg-gray-800/30 backdrop-blur-sm rounded-2xl overflow-hidden border border-gray-700/50 hover:border-purple-500/50 transition-all group"
            >
              <div className="relative overflow-hidden">
                <div className="bg-gray-200 border-2 border-dashed rounded-xl w-full h-48" />

                {project.featured && (
                  <div className="absolute top-4 right-4 bg-yellow-500 text-yellow-900 px-3 py-1 rounded-full text-sm font-medium flex items-center gap-1">
                    <FiStar size={14} /> Featured
                  </div>
                )}

                <div
                  className={`absolute inset-0 bg-gradient-to-t from-gray-900/80 to-transparent flex items-end p-6 transition-opacity ${
                    hoveredProject === project.id ? "opacity-100" : "opacity-0"
                  }`}
                >
                  <div className="flex gap-3">
                    <a
                      href={project.github}
                      className="p-3 bg-white/10 backdrop-blur-sm rounded-full hover:bg-white/20 transition-colors"
                    >
                      <FiGithub size={20} />
                    </a>
                    <a
                      href={project.live}
                      className="p-3 bg-white/10 backdrop-blur-sm rounded-full hover:bg-white/20 transition-colors"
                    >
                      <FiExternalLink size={20} />
                    </a>
                  </div>
                </div>
              </div>

              <div className="p-6">
                <h3 className="text-xl font-bold mb-2">{project.title}</h3>
                <p className="text-gray-400 mb-4">{project.description}</p>

                <div className="flex flex-wrap gap-2">
                  {project.technologies.map((tech, techIndex) => (
                    <span
                      key={techIndex}
                      className="px-3 py-1 bg-purple-900/30 text-purple-400 rounded-full text-sm"
                    >
                      {tech}
                    </span>
                  ))}
                </div>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
