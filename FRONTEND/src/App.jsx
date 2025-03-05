import React, { useState } from "react";
import axios from "axios";
import "./App.css";

function App() {
  const [file, setFile] = useState(null);
  const [result, setResult] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setResult("");
    setError("");
  };

  const handleUpload = async () => {
    if (!file) {
      setError("Please select a file first.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    setLoading(true);
    setResult("");
    setError("");

    try {
      const response = await axios.post("http://localhost:8080/api/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setResult(response.data);
    } catch (error) {
      setError("Error uploading file. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Time Complexity Analyzer</h2>
        <div className="upload-box">
          <input type="file" id="fileInput" onChange={handleFileChange} />
          <label htmlFor="fileInput" className="upload-label">
            {file ? file.name : "Drag & Drop or Click to Upload"}
          </label>
        </div>
        <button onClick={handleUpload} disabled={loading}>
          {loading ? "Analyzing..." : "Upload & Analyze"}
        </button>
        {error && <p className="error">{error}</p>}
        {result && <p className="result">{result}</p>}
      </div>
    </div>
  );
}

export default App;