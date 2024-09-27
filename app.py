import os
from flask import Flask, request, jsonify
from volcenginesdkarkruntime import Ark
from flask_cors import CORS
app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})


# Initialize Ark client with the API key from the environment variable
client = Ark(
    api_key=os.environ.get("ARK_API_KEY"),
    base_url="https://ark.cn-beijing.volces.com/api/v3"  # Replace with the correct domain
)
@app.route("/", methods=["GET"])
def home():
    return "Welcome to the AI Conversation API"

@app.route("/ai/conversation", methods=["POST"])
def ai_conversation():
    print("Received request from Android app")  # Debugging print statement
    data = request.json
    print(f"Data received: {data}")  # Debugging print statement

    user_input = data.get("input")
    print(f"User input: {user_input}")  # Debugging print statement

    if not user_input:
        return jsonify({"error": "No input provided"}), 400

    # Send request to AI model
    completion = client.chat.completions.create(
        model="ep-20240925153653-zrnkk",  # Replace with your model ID
        messages=[
            {"role": "system", "content": "你是豆包，是由字节跳动开发的 AI 人工智能助手"},
            {"role": "user", "content": user_input},
        ]
    )

    # Extract AI response
    ai_response = completion.choices[0].message.content
    print(f"AI response: {ai_response}")  # Debugging print statement
    return jsonify({"response": ai_response})

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=5000)
