import { Editor } from "@monaco-editor/react";
import { useEffect, useState } from "react";

interface Props {
  onChange?: (code: string | undefined) => void;
  code: string | null;
}

function setEditorTheme(monaco: any): void {
  monaco.editor.defineTheme("card-labs", {
    base: "vs-dark",
    inherit: true,
    rules: [],
    colors: {},
  });
}

export default function CodeEditor({
  onChange = (c) => {},
  code = "",
}: Props): JSX.Element {
  const [_code, setCode] = useState(code);
  useEffect(() => {
    setCode(code);
  }, [code]);

  return (
    <div className="w-full h-[calc(100%_-_4rem_-_11rem)] bg-secondary">
      <Editor
        height={"100%"}
        defaultLanguage="javascript"
        theme="card-labs"
        value={_code ?? undefined}
        onChange={onChange}
        beforeMount={setEditorTheme}
      />
    </div>
  );
}
