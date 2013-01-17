# lein-validate

A Leiningen plugin example that mimics `lein test`.

## Usage

Run `lein install`, then put `[lein-validate "0.1"]` into the `:plugins` vector of your
`:user` profile in ~/.lein/profile.clj

It should look like:

```clojure
{:user {:plugins [[lein-validate "0.1"]]}}
```

Or for a per-project usage:

Put `[lein-validate "0.1"]` into the `:plugins` vector of your project.clj.

Then:

    $ lein validate

## License

Copyright © 2013 Lee Hinman & the Denver Clojure group

Distributed under the Eclipse Public License, the same as Clojure.
