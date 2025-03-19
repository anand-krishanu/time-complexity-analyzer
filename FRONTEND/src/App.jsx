import React, { useState } from "react";
import axios from "axios";
import "./App.css";

function App() {
  const [file, setFile] = useState(null);
  const [result, setResult] = useState("");
  const [scrapedCode, setScrapedCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setResult("");
    setScrapedCode("");
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
    setScrapedCode("");
    setError("");

    try {
      const response = await axios.post("http://localhost:8080/api/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setResult(response.data.analysis);
      setScrapedCode(response.data.scrapedCode);
    } catch (error) {
      setError("Error uploading file. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <header className="header">
        <h1 className="title">Time Complexity Analyzer</h1>
      </header>
      <div className="card-container">
        <div className="card upload-section">
          <h2>Upload Your Code</h2>
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
        <div className="card code-section">
          <h3>Scraped Code</h3>
          <pre className="code-box">{scrapedCode || "No code available."}</pre>
        </div>
      </div>
      <footer className="footer">
        <p>Krishanu Anand | The NorthCap University</p>
        <div className="social-icons">
          <a href="https://github.com/yourgithub" target="_blank" rel="noopener noreferrer">
            <img src="/assets/github-logo.png" alt="GitHub" className="icon" />
          </a>
          <a href="https://linkedin.com/in/yourlinkedin" target="_blank" rel="noopener noreferrer">
            <img src="/assets/linkedin-logo.png" alt="LinkedIn" className="icon" />
          </a>
        </div>
      </footer>
    </div>
  );
}

export default App;