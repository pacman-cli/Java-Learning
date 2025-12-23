"use client";

import { motion } from "framer-motion";
import {
  FaPython,
  FaJava,
  FaReact,
  FaNodeJs,
  FaDatabase,
  FaAws,
} from "react-icons/fa";
import {
  SiJavascript,
  SiTypescript,
  SiNextdotjs,
  SiSpring,
  SiMysql,
  SiMongodb,
  SiDocker,
  SiKubernetes,
} from "react-icons/si";

const skillsData = [
  {
    category: "Frontend",
    skills: [
      { name: "JavaScript", icon: SiJavascript, level: 90 },
      { name: "TypeScript", icon: SiTypescript, level: 85 },
      { name: "React", icon: FaReact, level: 90 },
      { name: "Next.js", icon: SiNextdotjs, level: 85 },
    ],
  },
  {
    category: "Backend",
    skills: [
      { name: "Node.js", icon: FaNodeJs, level: 85 },
      { name: "Spring Boot", icon: SiSpring, level: 80 },
      { name: "Python", icon: FaPython, level: 75 },
      { name: "Java", icon: FaJava, level: 80 },
    ],
  },
  {
    category: "Database & Cloud",
    skills: [
      { name: "MySQL", icon: SiMysql, level: 80 },
      { name: "MongoDB", icon: SiMongodb, level: 75 },
      { name: "AWS", icon: FaAws, level: 70 },
      { name: "Docker", icon: SiDocker, level: 75 },
    ],
  },
];

export default function Skills() {
  return (
    <section id="skills" className="py-20 px-4">
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
            Skills
          </span>
        </motion.h2>

        <div className="grid md:grid-cols-3 gap-8">
          {skillsData.map((category, categoryIndex) => (
            <motion.div
              key={category.category}
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: categoryIndex * 0.1 }}
              className="bg-gray-800/30 backdrop-blur-sm rounded-2xl p-6 border border-gray-700/50 hover:border-purple-500/50 transition-all"
            >
              <h3 className="text-xl font-bold mb-6 text-center text-purple-400">
                {category.category}
              </h3>

              <div className="space-y-6">
                {category.skills.map((skill, skillIndex) => {
                  const IconComponent = skill.icon;
                  return (
                    <div key={skill.name}>
                      <div className="flex items-center justify-between mb-2">
                        <div className="flex items-center space-x-2">
                          <IconComponent
                            className="text-purple-400"
                            size={20}
                          />
                          <span className="font-medium">{skill.name}</span>
                        </div>
                        <span className="text-sm text-gray-400">
                          {skill.level}%
                        </span>
                      </div>

                      <div className="h-2 bg-gray-700 rounded-full overflow-hidden">
                        <motion.div
                          initial={{ width: 0 }}
                          whileInView={{ width: `${skill.level}%` }}
                          viewport={{ once: true }}
                          transition={{
                            duration: 1,
                            delay: categoryIndex * 0.1 + skillIndex * 0.05,
                          }}
                          className="h-full bg-gradient-to-r from-purple-500 to-pink-500 rounded-full"
                        />
                      </div>
                    </div>
                  );
                })}
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
