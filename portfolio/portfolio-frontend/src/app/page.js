"use client";

import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import {
  FiSun,
  FiMoon,
  FiGithub,
  FiLinkedin,
  FiTwitter,
  FiMail,
  FiMenu,
  FiX,
} from "react-icons/fi";
import AOS from "aos";
import "aos/dist/aos.css";
import Skills from "@/components/Skills";
import Projects from "@/components/Projects";
import Experience from "@/components/Experience";
import Education from "@/components/Education";
import Contact from "@/components/Contact";
import Footer from "@/components/Footer";

export default function Home() {
  const [darkMode, setDarkMode] = useState(true);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    AOS.init({
      duration: 1000,
      once: true,
    });
  }, []);

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
  };

  const navItems = [
    "About",
    "Skills",
    "Projects",
    "Experience",
    "Education",
    "Contact",
  ];

  return (
    <div
      className={`min-h-screen transition-colors duration-300 ${
        darkMode ? "dark" : "light"
      }`}
    >
      {/* Navigation */}
      <nav className="fixed w-full py-6 px-4 md:px-8 flex justify-between items-center z-50 backdrop-blur-md bg-black/10">
        <motion.div
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5 }}
          className="text-2xl font-bold bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent"
        >
          Portfolio
        </motion.div>

        {/* Desktop Navigation */}
        <div className="hidden md:flex items-center space-x-6">
          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={toggleDarkMode}
            className="p-2 rounded-full bg-gray-800 dark:bg-gray-700 text-white"
          >
            {darkMode ? <FiSun size={20} /> : <FiMoon size={20} />}
          </motion.button>

          <div className="flex space-x-4">
            {navItems.map((item, index) => (
              <motion.a
                key={item}
                href={`#${item.toLowerCase()}`}
                initial={{ opacity: 0, y: -20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
                className="text-gray-300 hover:text-white dark:text-gray-400 dark:hover:text-white transition-colors"
              >
                {item}
              </motion.a>
            ))}
          </div>
        </div>

        {/* Mobile Navigation Toggle */}
        <div className="md:hidden flex items-center space-x-4">
          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={toggleDarkMode}
            className="p-2 rounded-full bg-gray-800 dark:bg-gray-700 text-white"
          >
            {darkMode ? <FiSun size={20} /> : <FiMoon size={20} />}
          </motion.button>

          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            className="p-2 rounded-full bg-gray-800 dark:bg-gray-700 text-white"
          >
            {mobileMenuOpen ? <FiX size={24} /> : <FiMenu size={24} />}
          </motion.button>
        </div>
      </nav>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="md:hidden fixed top-20 left-0 right-0 bg-gray-900/95 backdrop-blur-lg z-40 py-4 px-4"
        >
          <div className="flex flex-col space-y-4">
            {navItems.map((item) => (
              <a
                key={item}
                href={`#${item.toLowerCase()}`}
                onClick={() => setMobileMenuOpen(false)}
                className="text-gray-300 hover:text-white dark:text-gray-400 dark:hover:text-white transition-colors py-2"
              >
                {item}
              </a>
            ))}
          </div>
        </motion.div>
      )}

      {/* Hero Section */}
      <section className="min-h-screen flex flex-col justify-center items-center px-4 pt-20">
        <div className="max-w-6xl w-full grid md:grid-cols-2 gap-12 items-center">
          <motion.div
            data-aos="fade-right"
            initial={{ opacity: 0, x: -50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.8 }}
          >
            <h1 className="text-4xl md:text-6xl font-bold mb-4">
              Hi, I'm{" "}
              <span className="bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent">
                John Doe
              </span>
            </h1>
            <h2 className="text-2xl md:text-3xl text-gray-400 mb-6">
              Computer Science Engineering Student
            </h2>
            <p className="text-lg text-gray-500 dark:text-gray-300 mb-8 max-w-2xl">
              I create beautiful, functional, and user-centered digital
              experiences. Passionate about cutting-edge technology and
              innovative solutions.
            </p>

            <div className="flex flex-wrap gap-4">
              <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                className="px-8 py-3 bg-gradient-to-r from-purple-600 to-pink-600 rounded-lg font-medium text-white shadow-lg shadow-purple-500/20"
              >
                View Projects
              </motion.button>

              <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                className="px-8 py-3 border-2 border-purple-600 rounded-lg font-medium text-purple-600 dark:text-purple-400 hover:bg-purple-600 hover:text-white transition-colors"
              >
                Contact Me
              </motion.button>
            </div>

            <div className="flex space-x-6 mt-8">
              {[FiGithub, FiLinkedin, FiTwitter, FiMail].map((Icon, index) => (
                <motion.a
                  key={index}
                  href="#"
                  whileHover={{ y: -5 }}
                  className="text-gray-400 hover:text-white dark:text-gray-500 dark:hover:text-white transition-colors"
                >
                  <Icon size={24} />
                </motion.a>
              ))}
            </div>
          </motion.div>

          <motion.div
            data-aos="fade-left"
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.8 }}
            className="flex justify-center"
          >
            <div className="relative">
              <div className="w-64 h-64 md:w-80 md:h-80 rounded-full bg-gradient-to-r from-purple-500 to-pink-500 p-1 animate-pulse-slow">
                <div className="bg-gray-800 rounded-full w-full h-full flex items-center justify-center">
                  <div className="bg-gray-200 border-2 border-dashed rounded-xl w-16 h-16" />
                </div>
              </div>
              <div className="absolute -top-4 -right-4 w-24 h-24 rounded-full bg-blue-500 blur-xl opacity-30 animate-pulse"></div>
              <div className="absolute -bottom-4 -left-4 w-24 h-24 rounded-full bg-purple-500 blur-xl opacity-30 animate-pulse"></div>
            </div>
          </motion.div>
        </div>
      </section>

      {/* About Section */}
      <section id="about" className="py-20 px-4">
        <div className="max-w-6xl mx-auto">
          <motion.h2
            data-aos="fade-up"
            className="text-3xl md:text-4xl font-bold text-center mb-16"
          >
            About{" "}
            <span className="bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent">
              Me
            </span>
          </motion.h2>

          <div className="grid md:grid-cols-2 gap-12 items-center">
            <motion.div data-aos="fade-right" className="space-y-6">
              <p className="text-lg text-gray-600 dark:text-gray-300">
                I'm a passionate Computer Science Engineering student with
                expertise in full-stack development, machine learning, and cloud
                technologies. With a strong foundation in computer science
                principles and hands-on experience in various programming
                languages and frameworks.
              </p>

              <p className="text-lg text-gray-600 dark:text-gray-300">
                My journey in technology began 5 years ago, and since then I've
                worked on numerous projects ranging from web applications to
                AI-powered solutions. I thrive in collaborative environments and
                enjoy solving complex problems with elegant code.
              </p>

              <div className="flex flex-wrap gap-4 mt-8">
                <span className="px-4 py-2 bg-purple-900/30 text-purple-400 rounded-full">
                  Problem Solving
                </span>
                <span className="px-4 py-2 bg-pink-900/30 text-pink-400 rounded-full">
                  Team Collaboration
                </span>
                <span className="px-4 py-2 bg-blue-900/30 text-blue-400 rounded-full">
                  Continuous Learning
                </span>
              </div>
            </motion.div>

            <motion.div data-aos="fade-left" className="grid grid-cols-2 gap-4">
              {[
                { label: "Projects", value: "50+" },
                { label: "Experience", value: "3 Years" },
                { label: "Clients", value: "25+" },
                { label: "Contributions", value: "100+" },
              ].map((stat, index) => (
                <div
                  key={index}
                  className="bg-gray-800/50 backdrop-blur-sm p-6 rounded-xl border border-gray-700/50 hover:border-purple-500/50 transition-all"
                >
                  <div className="text-3xl font-bold bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent">
                    {stat.value}
                  </div>
                  <div className="text-gray-400 mt-2">{stat.label}</div>
                </div>
              ))}
            </motion.div>
          </div>
        </div>
      </section>

      {/* Skills Section */}
      <Skills />

      {/* Projects Section */}
      <Projects />

      {/* Experience Section */}
      <Experience />

      {/* Education Section */}
      <Education />

      {/* Contact Section */}
      <Contact />

      {/* Footer */}
      <Footer />
    </div>
  );
}
