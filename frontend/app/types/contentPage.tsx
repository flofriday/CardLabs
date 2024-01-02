interface Page<T> {
  first: boolean;
  empty: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
  content: T[];
}

export type { Page };
