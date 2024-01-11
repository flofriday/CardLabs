import { useEffect, useState } from "react";
import CodeMirror from "@uiw/react-codemirror";
import { langs } from "@uiw/codemirror-extensions-langs";
import { tags as t } from "@lezer/highlight";
import { createTheme, CreateThemeOptions } from "@uiw/codemirror-themes";

interface Props {
  onChange?: (code: string | undefined | null) => void;
  code: string | null;
}

const Colors = {
  background: "#3C5366",
  foreground: "#edecee",
  comments: "#79C37B",
  keywords: "#8487E4",
};

const cardLabsTheme = createTheme({
  theme: "dark",
  settings: {
    background: Colors.background,
    foreground: Colors.foreground,
    caret: "#a277ff",
    selection: "#5a51898f",
    selectionMatch: "#5a51898f",
    gutterBackground: Colors.background,
    gutterForeground: Colors.foreground,
    gutterBorder: "transparent",
    lineHighlight: "#a394f033",
  },
  styles: [
    { tag: t.keyword, color: "#a277ff" },
    {
      tag: [t.name, t.deleted, t.character, t.macroName],
      color: Colors.foreground,
    },
    { tag: [t.propertyName], color: "#ffca85" },
    {
      tag: [t.processingInstruction, t.string, t.inserted, t.special(t.string)],
      color: Colors.keywords,
    },
    { tag: [t.function(t.variableName), t.labelName], color: "#ffca85" },
    {
      tag: [t.color, t.constant(t.name), t.standard(t.name)],
      color: Colors.keywords,
    },
    { tag: [t.definition(t.name), t.separator], color: Colors.foreground },
    { tag: [t.className], color: "#82e2ff" },
    {
      tag: [t.number, t.changed, t.annotation, t.modifier, t.self, t.namespace],
      color: Colors.keywords,
    },
    { tag: [t.typeName], color: "#82e2ff" },
    { tag: [t.operator, t.operatorKeyword], color: "#a277ff" },
    { tag: [t.url, t.escape, t.regexp, t.link], color: "#61ffca" },
    { tag: [t.meta, t.comment], color: Colors.comments },
    { tag: t.strong, fontWeight: "bold" },
    { tag: t.emphasis, fontStyle: "italic" },
    { tag: t.link, textDecoration: "underline" },
    { tag: t.heading, fontWeight: "bold", color: "#a277ff" },
    { tag: [t.atom, t.bool, t.special(t.variableName)], color: "#edecee" },
    { tag: t.invalid, color: "#ff6767" },
    { tag: t.strikethrough, textDecoration: "line-through" },
  ],
});

export default function CodeEditor({
  onChange = (c) => {},
  code = "",
}: Props): JSX.Element {
  const [_code, setCode] = useState(code);
  useEffect(() => {
    setCode(code);
    onChange(code);
  }, [code, onChange]);

  return (
    <div className="w-full h-[calc(100%_-_4rem_-_11rem)] bg-secondary text-lg">
      <CodeMirror
        height={"100%"}
        basicSetup={{
          lineNumbers: true,
          bracketMatching: true,
          indentOnInput: true,
          closeBrackets: true,
        }}
        extensions={[langs.scheme()]}
        theme={cardLabsTheme}
        value={_code ?? undefined}
        onChange={onChange}
      />
    </div>
  );
}
