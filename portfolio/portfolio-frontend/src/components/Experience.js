"use client";

import { motion } from "framer-motion";
import { FiCalendar, FiMapPin, FiExternalLink } from "react-icons/fi";

const experiences = [
  {
    id: 1,
    title: "Software Engineering Intern",
    company: "TechCorp Inc.",
    location: "San Francisco, CA",
    period: "Jun 2023 - Present",
    description:
      "Developed scalable web applications using React and Node.js. Collaborated with cross-functional teams to deliver high-quality software solutions. Implemented CI/CD pipelines reducing deployment time by 40%.",
    technologies: ["React", "Node.js", "AWS", "Docker"],
  },
  {
    id: 2,
    title: "Full Stack Developer",
    company: "StartUpXYZ",
    location: "Remote",
    period: "Jan 2022 - May 2023",
    description:
      "Built and maintained multiple client websites and web applications. Led a team of 3 developers in delivering projects on time. Improved application performance by optimizing database queries and implementing caching strategies.",
    technologies: ["Next.js", "MongoDB", "Express", "Tailwind CSS"],
  },
  {
    id: 3,
    title: "Research Assistant",
    company: "University of Technology",
    location: "Boston, MA",
    period: "Sep 2021 - Dec 2021",
    description:
      "Conducted research on machine learning algorithms for natural language processing. Published findings in IEEE conference proceedings. Mentored undergraduate students in programming fundamentals.",
    technologies: ["Python", "TensorFlow", "NLP", "Research"],
  },
];

export default function Experience() {
  return (
    <section id="experience" className="py-20 px-4">
      <div className="max-w-6xl mx-auto">
        <motion.h2
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.5 }}
          className="text-3xl md:text-4xl font-bold text-center mb-16"
        >
          Work{" "}
          <span className="bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent">
            Experience
          </span>
        </motion.h2>

        <div className="relative">
          {/* Vertical timeline line */}
          <div className="absolute left-0 md:left-1/2 top-0 bottom-0 w-0.5 bg-gradient-to-b from-purple-500 to-pink-500 transform md:translate-x-[-1px]"></div>

          <div className="space-y-12">
            {experiences.map((exp, index) => (
              <motion.div
                key={exp.id}
                initial={{ opacity: 0, x: index % 2 === 0 ? -30 : 30 }}
                whileInView={{ opacity: 1, x: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
                className={`relative ${
                  index % 2 === 0
                    ? "md:pr-8 md:text-right"
                    : "md:pl-8 md:text-left"
                } md:w-1/2 ${index % 2 === 0 ? "md:ml-auto" : ""}`}
              >
                {/* Timeline dot */}
                <div className="absolute top-6 w-4 h-4 rounded-full bg-purple-500 border-4 border-gray-900 md:left-1/2 md:transform md:-translate-x-1/2"></div>

                <div className="bg-gray-800/30 backdrop-blur-sm rounded-2xl p-6 border border-gray-700/50 hover:border-purple-500/50 transition-all ml-8 md:ml-0">
                  <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
                    <h3 className="text-xl font-bold">{exp.title}</h3>
                    <span className="px-3 py-1 bg-purple-900/30 text-purple-400 rounded-full text-sm">
                      {exp.period}
                    </span>
                  </div>

                  <div className="flex flex-wrap items-center gap-4 mb-4 text-gray-400">
                    <div className="flex items-center gap-1">
                      <FiExternalLink size={16} />
                      <span>{exp.company}</span>
                    </div>
                    <div className="flex items-center gap-1">
                      <FiMapPin size={16} />
                      <span>{exp.location}</span>
                    </div>
                  </div>

                  <p className="text-gray-300 mb-4">{exp.description}</p>

                  <div className="flex flex-wrap gap-2">
                    {exp.technologies.map((tech, techIndex) => (
                      <span
                        key={techIndex}
                        className="px-3 py-1 bg-gray-700/50 text-gray-300 rounded-full text-sm"
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
      </div>
    </section>
  );
}
