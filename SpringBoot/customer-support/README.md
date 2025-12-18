# 🤖 AI Customer Support System

A modern customer support system powered by **Spring Boot**, **Spring AI**, and **Ollama** that provides intelligent responses using local LLMs. This system offers a complete solution with REST APIs, web interface, and vector-based document search capabilities.

![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-green.svg)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M2-orange.svg)
![Ollama](https://img.shields.io/badge/Ollama-Local%20LLM-purple.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)

## ✨ Features

- 🚀 **Local AI Processing** - No API costs, unlimited usage with Ollama
- 💬 **Intelligent Chat** - Context-aware responses using vector similarity search
- 📚 **Knowledge Base Management** - Load and manage support documents
- 🗄️ **Database Persistence** - MySQL integration with automatic document storage
- 🌐 **Web Interface** - Beautiful chat UI for easy interaction
- 🔌 **REST APIs** - Complete API endpoints for integration
- **Swagger Documentation** - Interactive API documentation
- 🔍 **Vector Search** - Semantic document similarity using embeddings
- 🔄 **Hot Reload** - Development-friendly with auto-restart
- 🛡️ **Duplicate Prevention** - Smart document deduplication

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Web Frontend                             │
│                 (React-like Chat UI)                       │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────┴───────────────────────────────────────┐
│                Spring Boot REST API                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐│
│  │ SupportController│  │AICustomerSupport│  │VectorStore   ││
│  │                 │  │    Service      │  │   Service    ││
│  └─────────────────┘  └─────────────────┘  └──────────────┘│
└─────────────────────┬───────────────────┬───────────────────┘
                      │                   │
            ┌─────────┴──────────┐       ┌┴──────────────────┐
            │   Ollama Server    │       │   MySQL Database │
            │ (Local LLM + Emb)  │       │ (Document Storage)│
            └────────────────────┘       └───────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- **Java 17+** - [Download here](https://adoptium.net/)
- **Maven 3.6+** - [Installation guide](https://maven.apache.org/install.html)
- **MySQL 8.0+** - [Download here](https://dev.mysql.com/downloads/)
- **Ollama** - [Download here](https://ollama.ai/download)

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/ai-customer-support.git
cd ai-customer-support
```

### 2. Set Up MySQL Database

Create a MySQL database:

```sql
CREATE DATABASE customer_support_ai;
CREATE USER 'your_username'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON customer_support_ai.* TO 'your_username'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application Properties

Update `src/main/resources/application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/customer_support_ai?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Install and Set Up Ollama

Install Ollama:

```bash
# macOS
brew install ollama

# Linux
curl -fsSL https://ollama.ai/install.sh | sh

# Windows - Download from https://ollama.ai/download
```

Start Ollama and pull required models:

```bash
# Start Ollama service
ollama serve

# In a new terminal, pull the models
ollama pull llama3.2:1b          # Chat model (1GB)
ollama pull nomic-embed-text     # Embedding model (274MB)
```

### 5. Run the Application

```bash
# Clean and compile
./mvnw clean compile

# Start the application
./mvnw spring-boot:run
```

The application will be available at:

- **Web Interface**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/support/health

## 📖 Usage Guide

### Web Interface

1. **Open** http://localhost:8080 in your browser
2. **Load Knowledge Base** - Click "Load Basic KB" or "Load Extended KB"
3. **Start Chatting** - Type questions in the input field and press Enter
4. **Check Status** - Use "KB Status" and "Test Ollama" buttons for diagnostics

### API Endpoints

#### Core Endpoints

| Method | Endpoint                        | Description                       |
| ------ | ------------------------------- | --------------------------------- |
| GET    | `/api/support/health`           | Health check                      |
| GET    | `/api/support/test-ollama`      | Test Ollama connectivity          |
| POST   | `/api/support/ask`              | Ask a question                    |
| POST   | `/api/support/load-documents`   | Load basic knowledge base         |
| POST   | `/api/support/load-extended-kb` | Load comprehensive knowledge base |
| GET    | `/api/support/knowledge-stats`  | Get knowledge base statistics     |

#### Database Management (NEW! 🎉)

| Method | Endpoint                          | Description                          |
| ------ | --------------------------------- | ------------------------------------ |
| GET    | `/api/support/database-documents` | View all documents in MySQL database |
| DELETE | `/api/support/clear-database`     | Clear all documents from database    |

#### Example API Usage

**Ask a Question:**

```bash
curl -X POST http://localhost:8080/api/support/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is your return policy?"}'
```

**Load Knowledge Base:**

```bash
curl -X POST http://localhost:8080/api/support/load-documents
```

### Sample Questions to Try

- "What is your return policy?"
- "What payment methods do you accept?"
- "How long does shipping take?"
- "Tell me about your warranty policy"
- "How can I contact customer support?"
- "What products do you sell?"
- "Can I track my order?"

## 🔧 Configuration

### Ollama Configuration

The application uses these Ollama settings in `application.properties`:

```properties
# Ollama Configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2:1b
spring.ai.ollama.chat.options.temperature=0.7
spring.ai.ollama.embedding.options.model=nomic-embed-text
```

### Key Configuration Options

| Property                                    | Default          | Description                   |
| ------------------------------------------- | ---------------- | ----------------------------- |
| `spring.ai.ollama.chat.options.temperature` | 0.7              | Response creativity (0.0-1.0) |
| `spring.ai.ollama.chat.options.model`       | llama3.2:1b      | Chat model name               |
| `spring.ai.ollama.embedding.options.model`  | nomic-embed-text | Embedding model name          |

## 📊 Available Models

### Chat Models (Choose one)

- `llama3.2:1b` - Fast, lightweight (1GB)
- `llama3.2:3b` - Better quality (2GB)
- `mistral:7b` - High quality (4GB)
- `codellama:7b` - Code-focused (4GB)

### Embedding Models (Choose one)

- `nomic-embed-text` - Recommended (274MB)
- `all-minilm` - Alternative (23MB)
- `mxbai-embed-large` - High quality (669MB)

To switch models:

```bash
# Pull new model
ollama pull mistral:7b

# Update application.properties
spring.ai.ollama.chat.options.model=mistral:7b
```

## 🛠️ Development

### Project Structure

```
src/
├── main/
│   ├── java/com/pacman/customersupport/
│   │   ├── config/
│   │   │   └── AIConfig.java              # AI configuration
│   │   ├── controller/
│   │   │   └── SupportController.java     # REST endpoints
│   │   ├── dto/
│   │   │   ├── SupportRequest.java        # Request DTOs
│   │   │   └── SupportResponse.java       # Response DTOs
│   │   ├── entity/
│   │   │   └── DocumentChunk.java         # JPA entities
│   │   ├── repository/
│   │   │   └── DocumentChunkRepository.java # Data access
│   │   ├── service/
│   │   │   ├── AICustomerSupportService.java # AI logic
│   │   │   └── VectorStoreService.java    # Vector operations
│   │   └── CustomerSupportApplication.java # Main class
│   └── resources/
│       ├── application.properties         # Configuration
│       └── static/
│           └── index.html                # Web interface
└── test/
    └── java/com/pacman/customersupport/
        └── CustomerSupportApplicationTests.java
```

### Adding New Knowledge

To add new knowledge to the system:

1. **Via Code** - Edit `VectorStoreService.loadInitialDocuments()` or `loadExtendedKnowledgeBase()`
2. **Via Database** - Insert into `document_chunks` table
3. **Via API** - Create new endpoint to load custom documents

### 🗄️ Database Persistence (NEW!)

The system now includes **full database persistence** for document storage:

**Features:**

- **Automatic Saving** - Documents are automatically saved to MySQL during knowledge base loading
- **Duplicate Prevention** - Smart duplicate detection prevents redundant storage
- **Database Viewing** - View all stored documents with content previews
- **Database Management** - Clear database when needed
- **Statistics Integration** - Knowledge base stats show both vector store and database counts

**Database Schema:**

```sql
-- document_chunks table
CREATE TABLE document_chunks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    source_document VARCHAR(255),
    metadata_json TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Usage Examples:**

```bash
# View database contents
curl http://localhost:8080/api/support/database-documents

# Check statistics (includes database count)
curl http://localhost:8080/api/support/knowledge-stats

# Clear database if needed
curl -X DELETE http://localhost:8080/api/support/clear-database
```

### Extending the System

**Add New Models:**

```bash
ollama pull your-preferred-model
# Update application.properties
```

**Add New Endpoints:**

```java
@GetMapping("/custom-endpoint")
public ResponseEntity<?> customEndpoint() {
    // Your logic here
}
```

## 🐛 Troubleshooting

### Common Issues

**1. "Model not found" Error**

```bash
# Solution: Pull the required models
ollama pull llama3.2:1b
ollama pull nomic-embed-text
```

**2. Ollama Connection Error**

```bash
# Solution: Start Ollama service
ollama serve

# Check if running
curl http://localhost:11434/api/tags
```

**3. MySQL Connection Error**

- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database exists

**4. Application Won't Start**

```bash
# Check Java version
java --version  # Should be 17+

# Clean rebuild
./mvnw clean compile
```

### Debug Mode

Enable debug logging in `application.properties`:

```properties
logging.level.com.pacman.customersupport=DEBUG
logging.level.org.springframework.ai=DEBUG
```

### Health Checks

| URL                                      | Expected Response                                  |
| ---------------------------------------- | -------------------------------------------------- |
| http://localhost:8080/api/support/health | "Customer Support Service is running with Ollama!" |
| http://localhost:11434/api/tags          | JSON with available models                         |
| http://localhost:8080/swagger-ui.html    | Swagger documentation                              |

## 📈 Performance Tips

1. **Model Selection**: Use smaller models (`llama3.2:1b`) for faster responses
2. **Memory**: Ensure 4GB+ RAM available for Ollama
3. **CPU**: More CPU cores = faster generation
4. **Temperature**: Lower temperature (0.3-0.5) for more consistent responses
5. **Vector Store**: Use dedicated embedding models for better search

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Spring AI](https://spring.io/projects/spring-ai) - AI integration framework
- [Ollama](https://ollama.ai/) - Local LLM runtime
- [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
- [Llama](https://llama.meta.com/) - Meta's LLM family
- [Nomic](https://www.nomic.ai/) - Embedding models

## 📞 Support

If you encounter any issues:

1. Check the [Troubleshooting](#-troubleshooting) section
2. Review [GitHub Issues](https://github.com/your-username/ai-customer-support/issues)
3. Create a new issue with detailed information

---

**Made with ❤️ using Spring Boot + Ollama**

_No API keys required • Completely free • Privacy-focused • Works offline_
