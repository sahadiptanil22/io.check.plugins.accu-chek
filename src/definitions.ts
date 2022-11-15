export interface AccuChekGuidePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
