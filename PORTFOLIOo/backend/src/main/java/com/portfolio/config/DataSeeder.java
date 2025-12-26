package com.portfolio.config;

import com.portfolio.model.*;
import com.portfolio.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(SkillRepository skills, ProjectRepository projects,
            EducationRepository education, ExperienceRepository experience) {
        return args -> {
            if (skills.count() == 0) {
                skills.saveAll(List.of(
                        new Skill("Java", 90, "Backend"),
                        new Skill("Spring Boot", 85, "Backend"),
                        new Skill("Next.js", 82, "Frontend"),
                        new Skill("React", 84, "Frontend"),
                        new Skill("TypeScript", 80, "Frontend"),
                        new Skill("Tailwind CSS", 78, "Frontend"),
                        new Skill("Node.js", 76, "Backend"),
                        new Skill("MySQL", 75, "Database"),
                        new Skill("PostgreSQL", 70, "Database"),
                        new Skill("Docker", 72, "DevOps"),
                        new Skill("Kubernetes", 60, "DevOps"),
                        new Skill("Git/GitHub", 88, "Tooling")));
            }
            if (projects.count() == 0) {
                projects.saveAll(List.of(
                        new Project("AI Study Assistant",
                                "LLM-powered note summarizer and Q&A with semantic search and flashcards.",
                                List.of("AI", "Frontend"), "https://github.com/example/ai-study-assistant",
                                "https://demo.example.com/ai-study", null),
                        new Project("Portfolio Engine",
                                "Modern portfolio powered by Next.js + Spring Boot with animated UI and dark/light mode.",
                                List.of("Frontend", "Backend"), "https://github.com/example/portfolio-engine", null,
                                null),
                        new Project("Microservice Catalog",
                                "Event-driven product catalog with CQRS, Kafka, and resilient patterns.",
                                List.of("Backend", "Systems"), "https://github.com/example/microservice-catalog", null,
                                null),
                        new Project("Vision Dashboard",
                                "Realtime dashboard using WebSockets with animated charts and alerting.",
                                List.of("Frontend", "Systems"), "https://github.com/example/vision-dashboard",
                                "https://demo.example.com/vision", null),
                        new Project("DevOps Toolkit",
                                "CI/CD pipelines with Docker, GitHub Actions, and Kubernetes manifests.",
                                List.of("DevOps"), "https://github.com/example/devops-toolkit", null, null),
                        new Project("Mobile Companion",
                                "React Native companion app syncing with backend via REST and push notifications.",
                                List.of("Mobile", "Backend"), "https://github.com/example/mobile-companion", null,
                                null)));
            }
            if (education.count() == 0) {
                education.saveAll(List.of(
                        new Education("Tech University", "B.Tech", "Computer Science & Engineering", 2022, 2026),
                        new Education("Open Learning Institute", "Certification", "Cloud Native Development", 2024,
                                2024)));
            }
            if (experience.count() == 0) {
                experience.saveAll(List.of(
                        new Experience("Open Source", "Contributor", "2024-01-01", null,
                                "Contributed to tooling and UI libraries, resolved issues, and improved docs."),
                        new Experience("Freelance", "Full-stack Developer", "2023-06-01", "2024-03-31",
                                "Built performant web apps, integrated payment gateways, and optimized SEO."),
                        new Experience("Campus Labs", "Research Assistant", "2022-09-01", "2023-05-31",
                                "Worked on distributed systems prototyping and benchmarking.")));
            }
        };
    }
}
