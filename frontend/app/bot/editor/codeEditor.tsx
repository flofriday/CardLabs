import { Editor } from "@monaco-editor/react";
import { useEffect, useState } from "react";

interface Props {
  onChange?: (code: string | undefined | null) => void;
  code: string | null;
}

function setEditorTheme(monaco: any): void {
  monaco.editor.defineTheme("card-labs", {
    base: "vs-dark",
    inherit: true,
    rules: [
      {
        token: "comment",
        foreground: "5CBC8E",
      },
    ],
    colors: {
      "editor.foreground": "#FEF9EC",
      "editorLineNumber.foreground": "#FEF9EC",
      "editor.background": "#3C5366",
      "editor.lineHighlightBorder": "#FEF9EC20",
    },
    fontSize: 40,
  });
}

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
    <div className="w-full h-[calc(100%_-_4rem_-_11rem)] bg-secondary">
      <Editor
        height={"100%"}
        defaultLanguage="scheme"
        options={{
          fontSize: 20,
        }}
        theme="card-labs"
        value={_code ?? undefined}
        onChange={onChange}
        beforeMount={setEditorTheme}
      />
    </div>
  );
}
