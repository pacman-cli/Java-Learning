"use client";

import { motion } from "framer-motion";
import { FiCalendar, FiMapPin, FiAward } from "react-icons/fi";

const education = [
  {
    id: 1,
    degree: "Bachelor of Science in Computer Science",
    institution: "Stanford University",
    location: "Stanford, CA",
    period: "2020 - 2024",
    gpa: "3.8/4.0",
    description:
      "Specialized in Artificial Intelligence and Machine Learning. Completed capstone project on neural networks for computer vision applications.",
    courses: [
      "Data Structures & Algorithms",
      "Machine Learning",
      "Computer Networks",
      "Database Systems",
    ],
  },
  {
    id: 2,
    degree: "Higher Secondary Certificate",
    institution: "Greenwood High School",
    location: "San Jose, CA",
    period: "2018 - 2020",
    gpa: "4.0/4.0",
    description:
      "Graduated with honors. Participated in mathematics and computer science olympiads.",
    courses: [
      "Calculus",
      "Physics",
      "Computer Science Principles",
      "Statistics",
    ],
  },
];

export default function Education() {
  return (
    <section id="education" className="py-20 px-4">
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
            Education
          </span>
        </motion.h2>

        <div className="grid md:grid-cols-2 gap-8">
          {education.map((edu, index) => (
            <motion.div
              key={edu.id}
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
              className="bg-gray-800/30 backdrop-blur-sm rounded-2xl p-6 border border-gray-700/50 hover:border-purple-500/50 transition-all"
            >
              <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
                <h3 className="text-xl font-bold">{edu.degree}</h3>
                <span className="px-3 py-1 bg-purple-900/30 text-purple-400 rounded-full text-sm">
                  {edu.period}
                </span>
              </div>

              <div className="flex flex-wrap items-center gap-4 mb-4 text-gray-400">
                <div className="flex items-center gap-1">
                  <FiAward size={16} />
                  <span>{edu.institution}</span>
                </div>
                <div className="flex items-center gap-1">
                  <FiMapPin size={16} />
                  <span>{edu.location}</span>
                </div>
              </div>

              <p className="text-gray-300 mb-4">{edu.description}</p>

              <div className="mb-4">
                <span className="font-medium text-purple-400">GPA:</span>{" "}
                {edu.gpa}
              </div>

              <div>
                <h4 className="font-medium mb-2">Key Courses:</h4>
                <div className="flex flex-wrap gap-2">
                  {edu.courses.map((course, courseIndex) => (
                    <span
                      key={courseIndex}
                      className="px-3 py-1 bg-gray-700/50 text-gray-300 rounded-full text-sm"
                    >
                      {course}
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
