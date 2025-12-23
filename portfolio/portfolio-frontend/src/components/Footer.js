"use client";

import { motion } from "framer-motion";
import { FiGithub, FiLinkedin, FiTwitter, FiMail } from "react-icons/fi";

export default function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="py-12 px-4 border-t border-gray-800">
      <div className="max-w-6xl mx-auto">
        <div className="flex flex-col md:flex-row justify-between items-center gap-8">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.5 }}
          >
            <div className="text-2xl font-bold bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent mb-2">
              Portfolio
            </div>
            <p className="text-gray-400">
              Creating exceptional digital experiences
            </p>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.5, delay: 0.1 }}
            className="flex gap-6"
          >
            {[FiGithub, FiLinkedin, FiTwitter, FiMail].map((Icon, index) => (
              <motion.a
                key={index}
                href="#"
                whileHover={{ y: -5 }}
                className="text-gray-400 hover:text-white transition-colors"
              >
                <Icon size={24} />
              </motion.a>
            ))}
          </motion.div>
        </div>

        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          viewport={{ once: true }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="border-t border-gray-800 mt-12 pt-8 text-center text-gray-500"
        >
          <p>&copy; {currentYear} John Doe. All rights reserved.</p>
          <p className="mt-2 text-sm">
            Designed and built with ❤️ using Next.js, Tailwind CSS, and Framer
            Motion
          </p>
        </motion.div>
      </div>
    </footer>
  );
}
