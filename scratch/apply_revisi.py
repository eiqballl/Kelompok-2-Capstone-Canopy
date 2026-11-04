import re

def process_admin_frame():
    # 1. AdminFrame.form
    with open('src/main/java/view/AdminFrame.form', 'r', encoding='utf-8') as f:
        form = f.read()

    # Change splitEval dividerLocation from 420 to 340
    # In splitEval component:
    form = re.sub(
        r'(<Container class="javax.swing.JSplitPane" name="splitEval">\s*<Properties>\s*<Property name="dividerLocation" type="int" value=")420(" />)',
        r'\g<1>340\g<2>',
        form
    )

    # In pnlFormPegawai, add chkShowPasswordPgw if not already there
    if 'chkShowPasswordPgw' not in form:
        # Add to horizontal layout
        form = form.replace(
            '<Component id="txtPasswordPgw" alignment="0" max="32767" attributes="0" />',
            '<Component id="txtPasswordPgw" alignment="0" max="32767" attributes="0" />\n                            <Component id="chkShowPasswordPgw" alignment="0" min="-2" max="-2" attributes="0" />'
        )
        # Add to vertical layout
        form = form.replace(
            '<Component id="txtPasswordPgw" min="-2" pref="32" max="-2" attributes="0" />',
            '<Component id="txtPasswordPgw" min="-2" pref="32" max="-2" attributes="0" />\n                          <EmptySpace max="-2" attributes="0" />\n                          <Component id="chkShowPasswordPgw" min="-2" max="-2" attributes="0" />'
        )
        # Add subcomponent
        sub_chk = """                    <Component class="javax.swing.JCheckBox" name="chkShowPasswordPgw">
                      <Properties>
                        <Property name="font" type="java.awt.Font" editor="org.netbeans.beaninfo.editors.FontEditor">
                          <Font name="Segoe UI" size="11" style="0" />
                        </Property>
                        <Property name="text" type="java.lang.String" value="Lihat Password" />
                      </Properties>
                      <Events>
                        <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="chkShowPasswordPgwActionPerformed" />
                      </Events>
                    </Component>"""
        form = form.replace('</SubComponents>\n                </Container>\n                <Container class="javax.swing.JPanel" name="pnlTabelPegawai">', sub_chk + '\n                  </SubComponents>\n                </Container>\n                <Container class="javax.swing.JPanel" name="pnlTabelPegawai">')

    with open('src/main/java/view/AdminFrame.form', 'w', encoding='utf-8') as f:
        f.write(form)
    print("AdminFrame.form updated!")

process_admin_frame()

