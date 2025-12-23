import React, { useEffect } from "react";
import Prism from "prismjs";
import "prismjs/themes/prism-tomorrow.css";
import "prismjs/components/prism-java";
import "prismjs/components/prism-javascript";
import "prismjs/components/prism-typescript";
import "prismjs/components/prism-python";
import "prismjs/components/prism-sql";
import "prismjs/components/prism-json";
import "prismjs/components/prism-markup";
import "prismjs/components/prism-css";
import "./CodeBlock.css";

interface CodeBlockProps {
    code: string;
    language?: string;
    className?: string;
    showLineNumbers?: boolean;
    title?: string;
}

const CodeBlock: React.FC<CodeBlockProps> = ({
    code,
    language = "java",
    className = "",
    showLineNumbers = false,
    title,
}) => {
    useEffect(() => {
        Prism.highlightAll();
    }, [code, language]);

    const codeBlockId = React.useId();

    return (
        <div className={`code-block-container ${className}`}>
            {title && (
                <div className="code-block-header bg-gray-800 text-white px-4 py-2 text-sm font-medium rounded-t-lg flex items-center justify-between">
                    <span>{title}</span>
                    <span className="text-xs text-gray-400 uppercase">
                        {language}
                    </span>
                </div>
            )}
            <div className="relative">
                <pre
                    className={`language-${language} ${showLineNumbers ? "line-numbers" : ""} ${
                        title ? "rounded-t-none" : "rounded-lg"
                    } !m-0 overflow-x-auto`}
                    style={{
                        backgroundColor: "#2d3748",
                        color: "#e2e8f0",
                    }}
                >
                    <code
                        id={codeBlockId}
                        className={`language-${language}`}
                        style={{
                            fontFamily:
                                'Fira Code, Monaco, Consolas, "Liberation Mono", "Courier New", monospace',
                            fontSize: "14px",
                            lineHeight: "1.5",
                        }}
                    >
                        {code}
                    </code>
                </pre>
                <button
                    onClick={() => navigator.clipboard.writeText(code)}
                    className="absolute top-2 right-2 p-2 text-gray-400 hover:text-white transition-colors duration-200 bg-gray-700 hover:bg-gray-600 rounded"
                    title="Copy to clipboard"
                >
                    <svg
                        className="w-4 h-4"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                    >
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={2}
                            d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"
                        />
                    </svg>
                </button>
            </div>
        </div>
    );
};

export default CodeBlock;
