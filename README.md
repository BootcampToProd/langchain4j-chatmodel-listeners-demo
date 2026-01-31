# 🤖 LangChain4j ChatModel Listeners

This repository demonstrates how to add observability, security, and metrics to your AI applications using LangChain4j ChatModel Listeners in Spring Boot.

📖 **Complete Guide**: For detailed explanations and a full code walkthrough, read our comprehensive tutorial.<br>
👉 [**LangChain4j ChatModel Listeners: A Complete Guide**](https://bootcamptoprod.com/langchain4j-chatmodel-listeners/)

🎥 **Video Tutorial**: Prefer hands-on learning? Watch our step-by-step implementation guide.<br>
👉 YouTube Tutorial - Coming soon!!
---

## ✨ What This Project Demonstrates

This application is a practical guide to **ChatModel Listeners**, covering how to intercept and act on LLM requests and responses:

- **`ContentModerationListener`**: A security listener that scans for sensitive keywords and PII before the request is sent.
- **`LoggingAndTimingListener`**: An observability listener that logs requests/responses and measures the exact latency of the LLM call.
- **`TokenUsageListener`**: A metrics listener that aggregates and tracks global token consumption for cost monitoring.
- **Listener Registration & Ordering**: Demonstrates how to register listeners and set execution order.

---

## 🛠️ Prerequisites

To run this application, you will need the following:

1. **OpenRouter API Key**: This project uses OpenRouter to access free AI models (like DeepSeek) via an OpenAI-compatible endpoint.
    - Sign up at [OpenRouter.ai](https://openrouter.ai/) to generate your free key.
2. **Setup Environment Variables**: Set your API key as an environment variable in your IDE or terminal:
```bash
# Set your OpenRouter API Key
export OPENROUTER_API_KEY=your_api_key_here
```
---

## 🚀 How to Run and Test

**For detailed instructions on how to set up, configure, and test the application, kindly go through our comprehensive article:**  
👉 [**Click here for Setup & Testing Instructions**](https://bootcamptoprod.com/langchain4j-chatmodel-listeners/)

---