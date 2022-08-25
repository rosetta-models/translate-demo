Example 5:

Two conditional mapping synonyms on a single attribute, each using "set" with a "when" clause that is predicated on the value of a xml element at the specified xml path.

The attribute Z->str2Field is conditionally set from:
- xml path a->b->c->e when the value at xml path a->b->c->d is "FISH"
- xml path a->b->c->f when the value at xml path a->b->c->d is "SAUSAGE"