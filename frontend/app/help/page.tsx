"use client";

import LeftPageHeader from "../components/leftPageHeader";
import TextItem from "../components/textItem";
import Robot, { RobotType } from "../components/robot";
import Markdown from "react-markdown";
import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";

export default function Help(): JSX.Element {
  const markdownUrlMap = new Map<string, string>([
    ["./documentation/botApi.md", "Bot API"],
    ["./documentation/createBot.md", "Creating a new bot"],
    ["./documentation/editBot.md", "Editing a bot"],
    ["./documentation/botTesting.md", "Testing your bot"],
    ["./documentation/scoring.md", "Scoring"],
    ["./documentation/debugging.md", "Debugging"],
    ["./documentation/cardScheme.md", "Programming Language"],
    ["./documentation/convenienceFunctions.md", "Convenience functions"],
  ]);

  const [markdownContent, setMarkdownContent] = useState(new Map());

  const fetchMarkdown = async (url: string): Promise<string> => {
    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`Failed to load markdown file ${url}`);
      }
      return await response.text();
    } catch (error) {
      throw new Error(`Failed to load markdown file: ${url}`);
    }
  };

  const loadAllMarkdownFiles = async (
    markdownUrlList: string[]
  ): Promise<void> => {
    const errorMessages: string[] = [];

    for (const markdownUrl of markdownUrlList) {
      try {
        const content = await fetchMarkdown(markdownUrl);
        setMarkdownContent((map) => new Map(map.set(markdownUrl, content)));
      } catch (error) {
        errorMessages.push((error as Error).message);
      }
    }

    if (errorMessages.length !== 0) {
      throw new Error(
        `Failed to load markdown files for: ${errorMessages.join(", ")}`
      );
    }
  };

  useEffect(() => {
    loadAllMarkdownFiles(Array.from(markdownUrlMap.keys())).catch((error) => {
      toast.error((error as Error).message);
      console.error((error as Error).message);
    });
  }, []);

  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Help" />

      <div className="flex flex-1">
        <div className="w-1/4 mt-48 p-12">
          <Robot type={RobotType.QUESTIONMARK} />
        </div>

        <div className="w-1/2 px-12 pt-56">
          <div className="flex flex-col justify-center items-center space-y-6 pb-11">
            {Array.from(markdownUrlMap.keys()).map((markdownUrl, index) => {
              return (
                <TextItem
                  title={markdownUrlMap.get(markdownUrl) ?? "Documentation"}
                  key={markdownUrl}
                >
                  <Markdown className="prose prose-invert max-w-none">
                    {markdownContent.get(markdownUrl)}
                  </Markdown>
                </TextItem>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}
