"use client";

import LeftPageHeader from "../components/leftPageHeader";
import TextItem from "../components/textItem";
import Robot, { RobotType } from "../components/robot";
import Markdown from "react-markdown";
import React, { useState, useEffect } from "react";

export default function Help(): JSX.Element {
  const [programmingLanguageContent, setProgrammingLanguageContent] =
    useState("");
  const [codeTemplateContent, setCodeTemplateContent] = useState("");
  const [convenienceFunctionsContent, setConvenienceFunctionsContent] =
    useState("");
  const [debuggingContent, setDebuggingContent] = useState("");
  const [botTestingContent, setBotTestingContent] = useState("");
  const [createNewBotContent, setCreateNewBotContent] = useState("");
  const [editBotContent, setEditBotContent] = useState("");

  const fetchMarkdown = async (url: string): Promise<string> => {
    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`Failed to fetch ${url}`);
      }
      return await response.text();
    } catch (error) {
      throw new Error(
        `Error fetching Markdown file (${url}): ${(error as Error).message}`
      );
    }
  };

  const loadAllMarkdownFiles = async (): Promise<void> => {
    try {
      setCodeTemplateContent(
        await fetchMarkdown("./documentation/codeTemplate.md")
      );
      setProgrammingLanguageContent(
        await fetchMarkdown("./documentation/cardScheme.md")
      );

      setConvenienceFunctionsContent(
        await fetchMarkdown("./documentation/convenienceFunctions.md")
      );
      setDebuggingContent(await fetchMarkdown("./documentation/debugging.md"));
      setBotTestingContent(
        await fetchMarkdown("./documentation/botTesting.md")
      );
      setCreateNewBotContent(
        await fetchMarkdown("./documentation/createBot.md")
      );
      setEditBotContent(await fetchMarkdown("./documentation/editBot.md"));
    } catch (error) {
      throw new Error(
        `Error fetching Markdown files: ${(error as Error).message}`
      );
    }
  };

  useEffect(() => {
    loadAllMarkdownFiles().catch((error) => {
      console.error("Error fetching Markdown files:", error);
    });
  });

  return (
    <div>
      <LeftPageHeader title="Help" />

      <Robot type={RobotType.QUESTIONMARK} />
      <div className="flex flex-col justify-center items-center pt-52 space-y-6 pb-11">
        <TextItem title="Code template">
          <Markdown className="prose prose-invert max-w-none">
            {codeTemplateContent}
          </Markdown>
        </TextItem>
        <TextItem title="Convenience functions">
          <Markdown className="prose prose-invert max-w-none">
            {convenienceFunctionsContent}
          </Markdown>
        </TextItem>
        <TextItem title="Debugging">
          <Markdown className="prose prose-invert max-w-none">
            {debuggingContent}
          </Markdown>
        </TextItem>
        <TextItem title="Testing your bot">
          <Markdown className="prose prose-invert max-w-none">
            {botTestingContent}
          </Markdown>
        </TextItem>
        <TextItem title="Creating a new bot">
          <Markdown className="prose prose-invert max-w-none">
            {createNewBotContent}
          </Markdown>
        </TextItem>
        <TextItem title="Editing a bot">
          <Markdown className="prose prose-invert max-w-none">
            {editBotContent}
          </Markdown>
        </TextItem>
        <TextItem title="Programming Language">
          <Markdown className="prose prose-invert max-w-none">
            {programmingLanguageContent}
          </Markdown>
        </TextItem>
      </div>
    </div>
  );
}
