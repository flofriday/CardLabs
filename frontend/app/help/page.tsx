"use client";

import LeftPageHeader from "../components/leftPageHeader";
import TextItem from "../components/textItem";
import Robot, { RobotType } from "../components/robot";
import Markdown from "react-markdown";
import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";

export default function Help(): JSX.Element {
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

    if (errorMessages.length != 0) {
      throw new Error(
        `Failed to load markdown files for: ${errorMessages.join(", ")}`
      );
    }
  };

  useEffect(() => {
    const markdownUrlList = [
      "./documentation/codeTemplate.md",
      "./documentation/cardScheme.md",
      "./documentation/convenienceFunctions.md",
      "./documentation/debugging.md",
      "./documentation/botTesting.md",
      "./documentation/createBot.md",
      "./documentation/editBot.md",
    ];
    loadAllMarkdownFiles(markdownUrlList).catch((error) => {
      toast.error((error as Error).message);
      console.error((error as Error).message);
    });
  }, []);

  return (
    <div>
      <LeftPageHeader title="Help" />

      <Robot type={RobotType.QUESTIONMARK} />
      <div className="flex flex-col justify-center items-center pt-52 space-y-6 pb-11">
        <TextItem title="Code template">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent.get("./documentation/codeTemplate.md")}
          </Markdown>
        </TextItem>
        <TextItem title="Convenience functions">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent.get("./documentation/convenienceFunctions.md")}
          </Markdown>
        </TextItem>
        <TextItem title="Debugging">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent.get("./documentation/debugging.md")}
          </Markdown>
        </TextItem>
        <TextItem title="Testing your bot">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent.get("./documentation/botTesting.md")}
          </Markdown>
        </TextItem>
        <TextItem title="Creating a new bot">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent.get("./documentation/createBot.md")}
          </Markdown>
        </TextItem>
        <TextItem title="Editing a bot">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent.get("./documentation/editBot.md")}
          </Markdown>
        </TextItem>
        <TextItem title="Programming Language">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent.get("./documentation/cardScheme.md")}
          </Markdown>
        </TextItem>
      </div>
    </div>
  );
}
