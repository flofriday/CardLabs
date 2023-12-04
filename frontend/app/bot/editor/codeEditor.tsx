import { useEffect, useState } from "react";

interface Props {
  onChange?: (code: string | null) => void;
  code: string | null;
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
      <textarea
        className="h-full w-full bg-secondary p-3"
        value={_code ?? ""}
        onChange={(e) => {
          onChange(e.target.value);
        }}
      />
    </div>
  );
}
