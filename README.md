## How?

For supported lanterns, place the lantern on the side of the block, and the lantern will attach to that side.

## Compatibility

Lantern's that inherit from vanilla's lantern are automatically supported through a dynamic block
entity. This does **not** cover every lantern, but it should cover most.

<details>
<summary>For developers</summary>

If your lantern extends `LanternBlock` and does **not** override `getStateForPlacement` it will be
supported by the block entity. If you don't extend `LanternBlock` or you **do** override
`getStateForPlacement`, you should implement wall hanging on your own.

</details>
